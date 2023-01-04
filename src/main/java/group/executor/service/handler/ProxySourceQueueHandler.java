package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;

import java.util.Optional;

public interface ProxySourceQueueHandler {
    void addProxy(ProxyConfigHolder ... proxyConfigHolder);
    Optional<ProxyConfigHolder> pollProxy();
}
