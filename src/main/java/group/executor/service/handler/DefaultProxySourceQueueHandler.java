package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler {

    private final Queue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxyQueue.add(proxyConfigHolder);
    }

    @Override
    public Optional<ProxyConfigHolder> pollProxy() {
        return Optional.ofNullable(proxyQueue.poll());
    }
}
