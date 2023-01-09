package group.executor.service.proxy.manager;

import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.validator.ProxyValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultProxyLifecycleManager implements ProxyLifecycleManager {

    private final ProxySourceQueueHandler proxySourceQueueHandler;

    private final ProxyValidator proxyValidator;


    @Override
    public Optional<ProxyConfigHolder> getFirstValidProxy() {
        synchronized (proxySourceQueueHandler) {
            Optional<ProxyConfigHolder> proxyOptional;
            while ((proxyOptional = proxySourceQueueHandler.pollProxy()).isPresent()) {
                ProxyConfigHolder proxy = proxyOptional.get();
                if (proxyValidator.isValid(proxy))
                    return Optional.of(proxy);
            }
            return Optional.empty();
        }
    }
}
