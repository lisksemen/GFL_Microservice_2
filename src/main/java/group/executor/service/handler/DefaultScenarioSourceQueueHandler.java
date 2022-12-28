package group.executor.service.handler;

import group.executor.model.Scenario;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DefaultScenarioSourceQueueHandler implements ScenarioSourceQueueHandler{
    private final Queue<Scenario> scenarios = new LinkedBlockingQueue<>();

    @Override
    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    @Override
    public synchronized Optional<Scenario> pollScenario() {
        return Optional.ofNullable(scenarios.poll());
    }
}
