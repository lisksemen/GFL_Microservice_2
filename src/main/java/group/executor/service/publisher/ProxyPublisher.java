package group.executor.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import group.executor.service.handler.ProxySourceQueueHandler;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProxyPublisher {
    private MessagingService messagingService;

    private ObjectMapper objectMapper;

    private ProxySourceQueueHandler proxySourceQueueHandler;

    @Scheduled(fixedRate = 5000)
    public void publish() {
        try {
            OutboundMessage build = messagingService
                    .messageBuilder()
                    .build(objectMapper.writeValueAsString(proxySourceQueueHandler.pollProxy()));

            DirectMessagePublisher directMessagePublisher = messagingService.createDirectMessagePublisherBuilder().build();
            directMessagePublisher.publish(build, Topic.of("proxy"));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
