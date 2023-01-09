package group.executor.service.proxy.validator;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyNetworkConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultProxyValidator implements ProxyValidator {

    private final Request request;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProxyValidator.class);

    public DefaultProxyValidator(@Qualifier("testProxyRequest") Request request) {
        this.request = request;
    }

    @Override
    public boolean isValid(ProxyConfigHolder proxyConfigHolder) {
        if (proxyConfigHolder != null) {
            try {
                ProxyNetworkConfig proxyNetworkConfig = proxyConfigHolder.getProxyNetworkConfig();
                LOGGER.info("ProxyInValidator: " + proxyConfigHolder.getProxyNetworkConfig());
                LOGGER.info("-------------------------------------------------");
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyNetworkConfig.getHostName(), proxyNetworkConfig.getPort()));
                OkHttpClient client = new OkHttpClient.Builder()
                        .proxy(proxy)
                        .callTimeout(1, TimeUnit.SECONDS)
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