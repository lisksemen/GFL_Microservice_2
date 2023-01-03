package group.executor.service.handler;

import group.executor.model.Scenario;
import group.executor.model.Step;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class DefaultScenarioSourceQueueHandlerTest {

    private DefaultScenarioSourceQueueHandler defaultScenarioSourceQueueHandler;
    private Scenario scenario;

    @BeforeEach
    public void setUp() {
        defaultScenarioSourceQueueHandler = new DefaultScenarioSourceQueueHandler();
        Step step = new Step("sleep", "3");
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        scenario = new Scenario("test", "testSite", steps);

        defaultScenarioSourceQueueHandler.addScenario(scenario);
    }

    @Test
    void pollScenario() {
        Optional<Scenario> optionalScenario = defaultScenarioSourceQueueHandler.pollScenario();

        Assertions.assertEquals("test", optionalScenario.get().getName());
        Assertions.assertEquals("testSite", optionalScenario.get().getSite());

        Assertions.assertEquals("sleep", optionalScenario.get().getSteps().get(0).getAction());
        Assertions.assertEquals("3", optionalScenario.get().getSteps().get(0).getValue());
    }
}