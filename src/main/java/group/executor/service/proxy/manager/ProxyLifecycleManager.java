package group.executor.service.proxy.manager;

import group.executor.model.ProxyConfigHolder;

import java.util.Optional;

public interface ProxyLifecycleManager {

    Optional<ProxyConfigHolder> getFirstValidProxy();
}
