package group.executor.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:/messageVpn.properties")
public class MessageVPNPropertiesConfig {
    @Value("${message.vpn.host}")
    private String host;

    @Value("${message.vpn.name}")
    private String name;

    @Value("${message.vpn.username}")
    private String username;

    @Value("${message.vpn.password}")
    private String password;
}
