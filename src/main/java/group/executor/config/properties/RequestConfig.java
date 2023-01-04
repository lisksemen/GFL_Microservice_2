package group.executor.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:/request.properties")
public class RequestConfig {
    @Value("${proxy.sourceUrl.fixedRate}")
    private long fixedRate;
}
