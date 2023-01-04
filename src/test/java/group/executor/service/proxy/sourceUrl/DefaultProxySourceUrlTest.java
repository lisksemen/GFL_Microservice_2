package group.executor.service.proxy.sourceUrl;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DefaultProxySourceUrlTest {
    @SpyBean
    private ProxySourceUrl proxySourceUrl;

    @Test
    public void sendRequest() {
        Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAsserted(() ->
                verify(proxySourceUrl, Mockito.atLeast(3)).sendRequest());
    }
}