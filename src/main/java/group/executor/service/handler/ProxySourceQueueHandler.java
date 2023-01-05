package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

public interface ProxySourceQueueHandler {
    void addProxy(ProxyConfigHolder... proxyConfigHolder);

    Optional<ProxyConfigHolder> pollProxy();

    Collection<ProxyConfigHolder> pollAllProxy();

    boolean isEmpty();
    Queue<ProxyConfigHolder> getProxyQueue();
    boolean removeProxy(ProxyConfigHolder proxyConfigHolder);
}
