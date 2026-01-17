package com.contractmaster.evaluation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @GetMapping("/trigger-scenarios")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getTriggerScenarios() {
        // In a real application, this would fetch dynamic scenarios or documentation
        // For now, return static mock data.
        List<Map<String, String>> scenarios = Arrays.asList(
            Map.of("name", "Contract Creation", "description", "Rules are evaluated automatically when a new contract is created."),
            Map.of("name", "Contract Update", "description", "Rules are evaluated automatically when an existing contract is updated."),
            Map.of("name", "Scheduled Review", "description", "Rules can be scheduled to run periodically against active contracts.")
        );
        return ResponseEntity.ok(Map.of("scenarios", scenarios));
    }
}
