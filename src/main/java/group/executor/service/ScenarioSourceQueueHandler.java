package group.executor.service;

import group.executor.model.Scenario;

public interface ScenarioSourceQueueHandler {
    void addScenario(Scenario scenario);
    Scenario pollScenario();
}
