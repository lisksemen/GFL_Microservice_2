package group.executor.controller;

import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProxySourceController {

    private ProxySourceQueueHandler proxySourceQueueHandler;

    @PostMapping(value = "/proxy/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addProxy(@RequestBody ProxyConfigHolder proxyConfigHolder) {
        proxySourceQueueHandler.addProxy(proxyConfigHolder);

        return ResponseEntity.ok("Added successfully");
    }
}
