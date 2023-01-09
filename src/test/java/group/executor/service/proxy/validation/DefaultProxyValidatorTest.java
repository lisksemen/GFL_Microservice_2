package group.executor.service.proxy.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;
import group.executor.service.handler.DefaultProxySourceQueueHandler;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.sourceUrl.DefaultProxySourceUrl;
import group.executor.service.proxy.sourceUrl.ProxySourceUrl;
import group.executor.service.proxy.validator.DefaultProxyValidator;
import group.executor.service.proxy.validator.ProxyValidator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultProxyValidatorTest {
    private ProxySourceUrl proxySourceUrl;
    private ProxyValidator proxyValidator;
    private ProxyConfigHolder proxyConfigHolderNotWorking;
    private ProxySourceQueueHandler proxySourceQueueHandler;

    @BeforeEach
    void setUp() throws MalformedURLException {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.SECONDS)
                .build();
        URL url = new URL("https://public.freeproxyapi.com/api/Proxy/ProxyByType/0/3");
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        proxySourceQueueHandler = new DefaultProxySourceQueueHandler(new DefaultProxyValidator(request));

        proxySourceUrl = new DefaultProxySourceUrl(new ObjectMapper(), proxySourceQueueHandler, client, request);
        proxyConfigHolderNotWorking = new ProxyConfigHolder(new ProxyNetworkConfig("103.248.120.5", 8),
                new ProxyCredentials());
        proxyValidator = new DefaultProxyValidator(request);
    }

    @Test
    public void validateProxy() {
        boolean result1 = false;
        int count = 0;
        while (!result1 || count < 5) {
            proxySourceUrl.sendRequest();
            result1 = proxyValidator.isValid(proxySourceQueueHandler.pollProxy().get());
            count++;
        }
        boolean result2 = proxyValidator.isValid(proxyConfigHolderNotWorking);
        assertTrue(result1);
        assertFalse(result2);
    }
}