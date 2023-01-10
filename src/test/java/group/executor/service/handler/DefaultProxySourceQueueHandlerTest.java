package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


class DefaultProxySourceQueueHandlerTest {

    private DefaultProxySourceQueueHandler proxySourceQueueHandler;
    private ProxyConfigHolder proxyConfigHolder;

    @BeforeEach
    public void setUp() {
        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig("testPort", 8080);
        ProxyCredentials proxyCredentials = new ProxyCredentials("test", "password");
        proxyConfigHolder = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
        proxySourceQueueHandler = new DefaultProxySourceQueueHandler();

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