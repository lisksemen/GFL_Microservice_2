package group.executor.service.proxy.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;
import group.executor.service.handler.DefaultProxySourceQueueHandler;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.sourceUrl.DefaultProxySourceUrl;
import group.executor.service.proxy.sourceUrl.ProxySourceUrl;
import group.executor.service.proxy.validator.DefaultProxyValidation;
import group.executor.service.proxy.validator.ProxyValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultProxyValidationTest {
    private ProxySourceUrl proxySourceUrl;
    private ProxyValidation proxyValidation;
    private ProxyConfigHolder proxyConfigHolderNotWorking;
    private ProxySourceQueueHandler proxySourceQueueHandler;
    @BeforeEach
    void setUp() {
        proxySourceQueueHandler = new DefaultProxySourceQueueHandler();
        proxySourceUrl = new DefaultProxySourceUrl(new ObjectMapper(), proxySourceQueueHandler);
        proxyConfigHolderNotWorking = new ProxyConfigHolder(new ProxyNetworkConfig("103.248.120.5",8),
                new ProxyCredentials());
        proxyValidation = new DefaultProxyValidation();
    }

    @Test
    public void validateProxy(){
        boolean result1 = false;
        int count = 0;
        while (!result1 || count < 5){
            proxySourceUrl.sendRequest();
            result1 = proxyValidation.validateProxy(proxySourceQueueHandler.pollProxy().get());
            count++;
        }
        boolean result2 = proxyValidation.validateProxy(proxyConfigHolderNotWorking);
        assertTrue(result1);
        assertFalse(result2);
    }
}