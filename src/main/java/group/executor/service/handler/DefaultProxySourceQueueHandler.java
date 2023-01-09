package group.executor.service.handler;

import group.executor.model.ProxyConfigHolder;
import group.executor.service.proxy.validator.ProxyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@PropertySource("classpath:schedule.properties")
public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler {

    private final Queue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();
    private final ProxyValidator proxyValidator;

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultProxySourceQueueHandler.class);

    public DefaultProxySourceQueueHandler(ProxyValidator proxyValidator) {
        this.proxyValidator = proxyValidator;
    }

    @Override
    @Scheduled(fixedRateString = "${manager.fixedRate}")
    public void removeInvalidProxy() {

        LOGGER.info("Started removal");
        long start = System.currentTimeMillis();
        if (!isEmpty()) {
            for (ProxyConfigHolder proxyConfigHolder : proxyQueue) {
                boolean valid = proxyValidator.isValid(proxyConfigHolder);
                boolean networkConfig = proxyConfigHolder.getProxyNetworkConfig() != null ||
                        proxyConfigHolder.getProxyNetworkConfig().getHostName() != null ||
                        proxyConfigHolder.getProxyNetworkConfig().getPort() != null;
                if (!valid || !networkConfig) {
                    proxyQueue.remove(proxyConfigHolder);
                }
            }
        }
        long end = System.currentTimeMillis();
        LOGGER.info("Ended");
        LOGGER.info("Time took - " + (end - start) + " ms");

    }

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
    public boolean isEmpty() {
        return proxyQueue.isEmpty();
    }

    @Override
    public int size() {
        return proxyQueue.size();
    }
}
