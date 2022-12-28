package group.executor.controller;

import group.executor.model.Scenario;
import group.executor.service.ScenarioSourceQueueHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ScenarioSourceController {

    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    @PostMapping("/scenario/add")
    public ResponseEntity<?> addScenario(@RequestBody Scenario scenario) {
        scenarioSourceQueueHandler.addScenario(scenario);

        return ResponseEntity.ok("Added successfully");
    }
}
