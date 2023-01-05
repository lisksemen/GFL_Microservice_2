package group.executor.service.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.Scenario;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.handler.ScenarioSourceQueueHandler;
import group.executor.service.proxy.manager.ProxyLifecycleManager;
import group.executor.service.proxy.validator.ProxyValidator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GeneralVPNFacadePublisher implements VPNPublisher {

    private final ProxySourceQueueHandler proxySourceQueueHandler;

    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    private final ObjectMapper objectMapper;

    private final MessagingService messagingService;

    private final DirectMessagePublisher directMessagePublisher;

    private final ProxyLifecycleManager proxyLifecycleManager;


    @Override
    @Scheduled(fixedRate = 5000)
    public void publish() {
        if (!scenarioSourceQueueHandler.isEmpty()) {
            scenarioSourceQueueHandler.pollAllScenario().forEach(scenario -> {
                Optional<ProxyConfigHolder> firstValidProxy = proxyLifecycleManager.getFirstValidProxy();
                firstValidProxy.ifPresent(this::publishProxy);
                publishScenario(scenario);
            });
        }
    }

    private void publishScenario(Scenario scenario) {
        try {
            String scenarioJson = objectMapper.writeValueAsString(scenario);

            OutboundMessage message = messagingService
                    .messageBuilder()
                    .build(scenarioJson);

            directMessagePublisher.publish(message, Topic.of("scenario"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishProxy(ProxyConfigHolder proxyConfigHolder) {
        try {
            String proxyJson = objectMapper.writeValueAsString(proxyConfigHolder);

            OutboundMessage message = messagingService
                    .messageBuilder()
                    .build(proxyJson);

            directMessagePublisher.publish(message, Topic.of("proxy"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
