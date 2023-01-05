package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler {

    private final Queue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();

    @Override
    public void addProxy(ProxyConfigHolder... proxyConfigHolder) {
        Collections.addAll(proxyQueue, proxyConfigHolder);
    }

    @Override
    public Optional<ProxyConfigHolder> pollProxy() {
        return Optional.ofNullable(proxyQueue.poll());
    }

    @Override
    public Collection<ProxyConfigHolder> pollAllProxy() {
        HashSet<ProxyConfigHolder> result = new HashSet<>(proxyQueue);
        proxyQueue.clear();
        return result;
    }

    @Override
    public Queue<ProxyConfigHolder> getProxyQueue(){
        return proxyQueue;
    }

    @Override
    public boolean removeProxy(ProxyConfigHolder proxyConfigHolder){
        return proxyQueue.remove(proxyConfigHolder);
    }

    @Override
    public boolean isEmpty() {
        return proxyQueue.isEmpty();
    }
}
