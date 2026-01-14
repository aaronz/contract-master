package com.contract.master.controller;

import com.contract.master.service.AIService;
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
