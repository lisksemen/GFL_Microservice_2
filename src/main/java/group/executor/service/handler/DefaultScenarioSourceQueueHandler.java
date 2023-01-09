package group.executor.service.handler;

import group.executor.model.Scenario;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DefaultScenarioSourceQueueHandler implements ScenarioSourceQueueHandler {
    private final Queue<Scenario> scenarios = new LinkedBlockingQueue<>();

    @Override
    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    @Override
    public synchronized Optional<Scenario> pollScenario() {
        return Optional.ofNullable(scenarios.poll());
    }

    @Override
    public synchronized Collection<Scenario> pollAllScenario() {
        HashSet<Scenario> result = new HashSet<>(scenarios);
        scenarios.clear();
        return result;
    }

    @Override
    public boolean isEmpty() {
        return scenarios.isEmpty();
    }


}
