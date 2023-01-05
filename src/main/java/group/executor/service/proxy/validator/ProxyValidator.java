package group.executor.service.proxy.validator;

import group.executor.model.ProxyConfigHolder;

public interface ProxyValidator {
    boolean isValid(ProxyConfigHolder proxyConfigHolder);
}
