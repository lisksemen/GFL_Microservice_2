package group.executor.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import group.executor.service.handler.ScenarioSourceQueueHandler;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScenarioPublisher {
    private MessagingService messagingService;

    private DirectMessagePublisher directMessagePublisher;

    private ObjectMapper objectMapper;

    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    @Scheduled(fixedRate = 5000)
    public void publish() {
        scenarioSourceQueueHandler.pollAllScenario().forEach(scenario -> {
            try {
                String scenarioJson = objectMapper.writeValueAsString(scenario);

                OutboundMessage message = messagingService
                        .messageBuilder()
                        .build(scenarioJson);

                directMessagePublisher.publish(message, Topic.of("scenario"));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
