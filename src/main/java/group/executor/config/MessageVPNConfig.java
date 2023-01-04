package group.executor.config;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import group.executor.config.properties.MessageVPNPropertiesConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@AllArgsConstructor
public class MessageVPNConfig {

    private final MessageVPNPropertiesConfig messageVPNPropertiesConfig;

    @Bean
    public MessagingService messagingService() {
        final Properties properties = new Properties();
        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, messageVPNPropertiesConfig.getHost());
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, messageVPNPropertiesConfig.getName());
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, messageVPNPropertiesConfig.getUsername());
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, messageVPNPropertiesConfig.getPassword());
        return MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build()
                .connect();
    }
}
