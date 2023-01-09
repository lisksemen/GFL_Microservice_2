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

import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource("classpath:schedule.properties")
public class DefaultProxySourceUrl implements ProxySourceUrl {
    private final ObjectMapper objectMapper;
    private final ProxySourceQueueHandler proxySourceQueueHandler;

    private final OkHttpClient okHttpClient;

    private final Request request;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProxySourceUrl.class);
    public DefaultProxySourceUrl(ObjectMapper objectMapper, ProxySourceQueueHandler proxySourceQueueHandler,
                                 @Qualifier("okHttpClientTestProxy") OkHttpClient okHttpClient,
                                 @Qualifier("requestProxy") Request request) {
        this.objectMapper = objectMapper;
        this.proxySourceQueueHandler = proxySourceQueueHandler;
        this.okHttpClient = okHttpClient;
        this.request = request;
    }

    @Async
    @Override
    @Scheduled(fixedRateString = "${sourceUrl.fixedRate}")
    public void sendRequest() {
        if (proxySourceQueueHandler.size() >= 5)
            return;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null && response.isSuccessful()){
                proxySourceQueueHandler.addProxy(getProxyFromResponse(Objects.requireNonNull(response.body()).string()));
            }
            response.close();
        } catch (IOException e) {
            throw new RuntimeException("Message: " + e.getMessage());
        }
    }

    private ProxyConfigHolder getProxyFromResponse(String response) throws JsonProcessingException {
        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();
        Map<String, Object> resultResponse = objectMapper.readValue(response, new TypeReference<>() {
        });
        for (String s : resultResponse.keySet()) {
            if (s.contains("host")) {
                proxyNetworkConfig.setHostName(resultResponse.get(s).toString());
            } else if (s.contains("port")) {
                int port = Integer.parseInt(resultResponse.get(s).toString());
                proxyNetworkConfig.setPort(port);
            }
        }
        return new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials("empty", "empty"));
    }
}
