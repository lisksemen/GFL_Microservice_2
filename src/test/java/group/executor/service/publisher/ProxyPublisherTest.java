package group.executor.service.publisher;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.resources.Topic;
import com.solace.messaging.resources.TopicSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPublisherTest {

    private MessagingService messagingService;

    private DirectMessagePublisher directMessagePublisher;

    private DirectMessageReceiver directMessageReceiver;

    @BeforeEach
    void setUp() {
        final Properties properties = new Properties();
        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, "tcps://mr-connection-7nic02oyzh0.messaging.solace.cloud:55443");
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, "gfl-qa-automation-cluster");
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, "solace-cloud-client");
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, "524r85vnd5qq2n1tg7u6mv9q7s");
        messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build()
                .connect();

        directMessagePublisher = messagingService.createDirectMessagePublisherBuilder().build().start();

        directMessageReceiver = messagingService.createDirectMessageReceiverBuilder().withSubscriptions(TopicSubscription.of("test")).build().start();
    }

    @Test
    void publish() {
        directMessagePublisher.publish(messagingService.messageBuilder().build("123"), Topic.of("test"));
        InboundMessage inboundMessage = directMessageReceiver.receiveMessage();
        assertEquals(inboundMessage.getPayloadAsString(), "123");
    }
}