package group.executor.service.proxy;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;
import group.executor.service.proxy.validator.DefaultProxyValidation;
import group.executor.service.proxy.validator.ProxyValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultProxyValidationTest {
    private ProxyValidation proxyValidation;
    private ProxyConfigHolder proxyConfigHolder;
    @BeforeEach
    void setUp() {
        proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig("157.92.32.83",8080),
                new ProxyCredentials());
        proxyValidation = new DefaultProxyValidation();
    }

    @Test
    public void validateProxy(){
        boolean result = proxyValidation.validateProxy(proxyConfigHolder);
        System.out.println("Connection " + result);
        assertTrue(result);
    }
}