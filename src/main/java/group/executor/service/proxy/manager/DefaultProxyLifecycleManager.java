package group.executor.service.proxy.manager;

import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.validator.ProxyValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Queue;

@Service
@AllArgsConstructor
@PropertySource("classpath:schedule.properties")
public class DefaultProxyLifecycleManager implements ProxyLifecycleManager {

    private final ProxySourceQueueHandler proxySourceQueueHandler;

    private final ProxyValidator proxyValidator;

    @Override
    @Scheduled(fixedRateString = "${manager.fixedRate}")
    public void removeInvalidProxy() {
        synchronized (proxySourceQueueHandler){
            if(!proxySourceQueueHandler.isEmpty()){
                Queue<ProxyConfigHolder> proxyQueue = proxySourceQueueHandler.getProxyQueue();
                for (ProxyConfigHolder proxyConfigHolder : proxyQueue) {
                    boolean valid = proxyValidator.isValid(proxyConfigHolder);
                    if (!valid){
                        proxySourceQueueHandler.removeProxy(proxyConfigHolder);
                    }
                }
            }
        }
    }

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
