package group.executor.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import group.executor.service.proxy.validator.ProxyValidator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        try {
            Optional<ProxyConfigHolder> prx = proxySourceQueueHandler.pollProxy();
            if (prx.isPresent() && proxyValidator.isValid(prx.get())) {

                String jsonProxy = objectMapper.writeValueAsString(prx.get());

                OutboundMessage build = messagingService
                        .messageBuilder()
                        .build(jsonProxy);

                directMessagePublisher.publish(build, Topic.of("proxy"));
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
