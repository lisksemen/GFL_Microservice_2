package group.executor.controller;

import group.executor.model.Scenario;
import group.executor.service.handler.ScenarioSourceQueueHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ScenarioSourceController {

    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    @PostMapping(value = "/scenario/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addScenario(@RequestBody Scenario scenario) {
        scenarioSourceQueueHandler.addScenario(scenario);

        return ResponseEntity.ok("Added successfully");
    }
}
