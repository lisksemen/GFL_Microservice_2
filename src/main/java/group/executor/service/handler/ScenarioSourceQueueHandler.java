package group.executor.service.handler;

import group.executor.model.Scenario;

import java.util.Collection;
import java.util.Optional;

public interface ScenarioSourceQueueHandler {
    void addScenario(Scenario scenario);
    Optional<Scenario> pollScenario();

    Collection<Scenario> pollAllScenario();

    boolean isEmpty();
}
