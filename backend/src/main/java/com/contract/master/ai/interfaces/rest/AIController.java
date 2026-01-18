package com.contract.master.ai.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.ai.application.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ai-upload")
    public Map<String, Object> uploadAndExtract(@RequestParam("file") MultipartFile file) throws IOException {
        return aiService.extractContractData("MOCK_ID", file);
    }
}
