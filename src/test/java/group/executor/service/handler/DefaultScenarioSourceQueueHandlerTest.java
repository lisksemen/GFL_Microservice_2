package group.executor.service.handler;

import group.executor.model.Scenario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class DefaultScenarioSourceQueueHandlerTest {

    private final Queue<Scenario> scenarios = new LinkedBlockingQueue<>();
    private Scenario scenario;

    @BeforeEach
    public void setUp() {
        scenario = new Scenario();
    }

    @Test
    public void addScenario() {
        scenarios.add(scenario);
        Assertions.assertEquals(1, scenarios.size());
    }

    @Test
    void pollScenario() {
        scenarios.poll();
        Assertions.assertEquals(0, scenarios.size());
    }
}