package group.executor.service.proxy.sourceUrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;
import group.executor.service.handler.ProxySourceQueueHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultProxySourceUrl implements ProxySourceUrl {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private ProxySourceQueueHandler proxySourceQueueHandler;

    public DefaultProxySourceUrl(ProxySourceQueueHandler proxySourceQueueHandler) {
        this.proxySourceQueueHandler = proxySourceQueueHandler;
    }
    @Async
    @Override
    @Scheduled(fixedRate = 5000)
    public void sendRequest() {
        sendRequestWithCallback(null);
    }

    public void sendRequestWithCallback(Runnable callback) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
            URL url = new URL("https://public.freeproxyapi.com/api/Proxy/ProxyByType/0/3");
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            String response = client.newCall(request).execute().body().string();
            proxySourceQueueHandler.addProxy(getProxyFromResponse(response));
            if (callback != null){
                 callback.run();
            }
        } catch (IOException e) {
            throw new RuntimeException("Message: " + e.getMessage());
        }
    }

    private ProxyConfigHolder getProxyFromResponse(String response) throws JsonProcessingException {
        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();
        Map<String, Object> resultResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
        for (String s : resultResponse.keySet()) {
            if (s.contains("host")){
                proxyNetworkConfig.setHostName(resultResponse.get(s).toString());
            } else if (s.contains("port")){
                int port = Integer.parseInt(resultResponse.get(s).toString());
                proxyNetworkConfig.setPort(port);
            }
        }
        return new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials("empty","empty"));
    }
}
