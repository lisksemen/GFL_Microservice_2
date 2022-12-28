package group.executor.service.proxy.validator;

import group.executor.model.ProxyConfigHolder;

public interface ProxyValidation {
    boolean validateProxy(ProxyConfigHolder proxyConfigHolder);
}
