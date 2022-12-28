package group.executor.service;

import group.executor.model.Scenario;
import org.springframework.stereotype.Service;

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
    public synchronized Scenario pollScenario() {
        return scenarios.poll();
    }
}
