package group.executor.service.proxy.validator;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyNetworkConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultProxyValidator implements ProxyValidator {
    @Override
    public boolean isValid(ProxyConfigHolder proxyConfigHolder) {
        if (proxyConfigHolder != null) {
            try {
                ProxyNetworkConfig proxyNetworkConfig = proxyConfigHolder.getProxyNetworkConfig();
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyNetworkConfig.getHostName(), proxyNetworkConfig.getPort()));
                OkHttpClient client = new OkHttpClient.Builder()
                        .proxy(proxy)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                URL url = new URL("https://www.google.com");
                Request request = new Request.Builder()
                        .get()
                        .url(url)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        return true;
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}