package group.executor.service.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.model.Scenario;
import group.executor.service.handler.ScenarioSourceQueueHandler;
import group.executor.service.proxy.manager.ProxyLifecycleManager;
import lombok.AllArgsConstructor;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GeneralVPNFacadePublisher implements VPNPublisher {

    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    private final ObjectMapper objectMapper;

    private final ProxyLifecycleManager proxyLifecycleManager;

    private final JmsTemplate jmsTemplate;


    @PostConstruct
    private void customizeJmsTemplate() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
        jmsTemplate.setConnectionFactory(ccf);
        jmsTemplate.setPubSubDomain(false);
    }


    @Override
    @Scheduled(fixedRate = 2000)
    public void publish() {
        Optional<Scenario> scenarioOptional;
        while ((scenarioOptional = scenarioSourceQueueHandler.pollScenario()).isPresent()) {
            Scenario scenario = scenarioOptional.get();
            Optional<ProxyConfigHolder> firstValidProxy = proxyLifecycleManager.getFirstValidProxy();
            firstValidProxy.ifPresent(this::publishProxy);
            publishScenario(scenario);
        }
    }

    private void publishScenario(Scenario scenario) {
        try {
            String scenarioJson = objectMapper.writeValueAsString(scenario);
            jmsTemplate.convertAndSend("scenario", scenarioJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishProxy(ProxyConfigHolder proxy) {
        try {
            String proxyJson = objectMapper.writeValueAsString(proxy);
            jmsTemplate.convertAndSend("proxy", proxyJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
