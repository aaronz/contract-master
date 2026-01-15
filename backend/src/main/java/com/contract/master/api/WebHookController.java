package com.contract.master.api;

import com.contract.master.service.CrmIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebHookController {

    @Autowired
    private CrmIntegrationService crmIntegrationService;

    @PostMapping("/{source}")
    public void receiveWebhook(@PathVariable String source, @RequestBody Map<String, Object> payload) {
        crmIntegrationService.syncContract(payload, source.toUpperCase());
    }
}
