package group.executor.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.validator.ProxyValidator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProxyPublisher {
    private MessagingService messagingService;

    private DirectMessagePublisher directMessagePublisher;

    private ObjectMapper objectMapper;

    private ProxySourceQueueHandler proxySourceQueueHandler;

    private ProxyValidator proxyValidator;

    @Scheduled(fixedRate = 5000)
    public void publish() {
        proxySourceQueueHandler.pollAllProxy().forEach(proxyConfigHolder -> {
            if (proxyValidator.isValid(proxyConfigHolder)) {
                try {
                    String jsonProxy = objectMapper.writeValueAsString(proxyConfigHolder);
                    OutboundMessage build = messagingService
                            .messageBuilder()
                            .build(jsonProxy);

                    directMessagePublisher.publish(build, Topic.of("proxy"));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
