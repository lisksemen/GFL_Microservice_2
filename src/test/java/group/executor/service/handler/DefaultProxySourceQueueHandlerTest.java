package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;

import group.executor.service.proxy.validator.DefaultProxyValidator;
import okhttp3.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;


class DefaultProxySourceQueueHandlerTest {

    private DefaultProxySourceQueueHandler proxySourceQueueHandler;
    private ProxyConfigHolder proxyConfigHolder;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig("testPort", 8080);
        ProxyCredentials proxyCredentials = new ProxyCredentials("test", "password");
        proxyConfigHolder = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
        URL url = new URL("https://public.freeproxyapi.com/api/Proxy/ProxyByType/0/3");
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        proxySourceQueueHandler = new DefaultProxySourceQueueHandler(new DefaultProxyValidator(request));

        proxySourceQueueHandler.addProxy(proxyConfigHolder);
    }

    @Test
    void pollProxy() {
        Optional<ProxyConfigHolder> optionalProxyConfigHolder = proxySourceQueueHandler.pollProxy();

        Assertions.assertEquals("testPort", optionalProxyConfigHolder.get().getProxyNetworkConfig().getHostName());
        Assertions.assertEquals(8080, optionalProxyConfigHolder.get().getProxyNetworkConfig().getPort());

        Assertions.assertEquals("test", optionalProxyConfigHolder.get().getProxyCredentials().getUsername());
        Assertions.assertEquals("password", optionalProxyConfigHolder.get().getProxyCredentials().getPassword());
    }
}