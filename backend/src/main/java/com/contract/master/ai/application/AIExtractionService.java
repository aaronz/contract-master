package com.contract.master.ai.application;

import com.contract.master.ai.domain.model.AISetting;
import com.contract.master.ai.domain.repository.AISettingRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIExtractionService {

    private static final Logger log = LoggerFactory.getLogger(AIExtractionService.class);

    @Autowired
    private AISettingRepository aiSettingRepository;

    @Autowired
    private com.contract.master.contract.domain.repository.ContractAttachmentRepository attachmentRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> extract(MultipartFile file, String contractId) {
        String currentTenant = TenantContext.getCurrentTenant();
        TenantId tenantId = TenantId.of(currentTenant != null ? currentTenant : "tenant-1");
        
        AISetting setting = aiSettingRepository.findByTenantId(tenantId).orElse(null);

        String textContent;
        try {
            textContent = extractTextFromFile(file);
            
            if (contractId != null && !contractId.isEmpty()) {
                saveAsAttachment(file, contractId, tenantId);
            }
        } catch (Exception e) {
            log.error("Failed to process file for extraction", e);
            return generateMockData(file.getOriginalFilename());
        }
        
        if (setting == null || "MOCK".equalsIgnoreCase(setting.getProvider()) || setting.getApiKey() == null || setting.getApiKey().isEmpty()) {
            log.info("AI not configured or in MOCK mode, returning mock data");
            return generateMockData(file.getOriginalFilename());
        }

        String prompt = setting.getExtractionPrompt();
        if (prompt == null || prompt.isEmpty()) {
            prompt = "You are a legal analyzer. Extract fields from the provided contract text (English or Chinese). " +
                     "Return ONLY a JSON object with keys: contractNo, contractName, partyAName, partyBName, contractAmount, contractType. " +
                     "If missing, use null.";
        }

        try {
            return callLLM(setting, prompt, textContent);
        } catch (Exception e) {
            log.error("LLM call failed", e);
            return generateMockData(file.getOriginalFilename());
        }
    }

    private void saveAsAttachment(MultipartFile file, String contractId, TenantId tenantId) throws IOException {
        com.contract.master.contract.domain.model.ContractAttachment attachment = new com.contract.master.contract.domain.model.ContractAttachment();
        attachment.setAttachmentId(java.util.UUID.randomUUID().toString());
        attachment.setContractId(contractId);
        attachment.setAttachmentName(file.getOriginalFilename());
        attachment.setFileSize(file.getSize());
        attachment.setFileFormat(file.getContentType());
        attachment.setStoragePath("uploads/" + attachment.getAttachmentId());
        attachment.setTenantId(tenantId);
        attachment.setUploadUser(TenantContext.getCurrentTenant());
        attachmentRepository.save(attachment);
        log.info("Saved contract attachment {} for contract {}", attachment.getAttachmentName(), contractId);
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        }
        return new String(file.getBytes());
    }

    private Map<String, Object> callLLM(AISetting setting, String prompt, String content) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", setting.getModelName());
        
        String limitedContent = content.length() > 10000 ? content.substring(0, 10000) : content;
        
        requestBody.put("messages", new Object[]{
            Map.of("role", "system", "content", prompt),
            Map.of("role", "user", "content", "Here is the contract text:\n\n" + limitedContent)
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + setting.getApiKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        log.info("Calling LLM provider {} at endpoint {} with model {}", setting.getProvider(), setting.getEndpointUrl(), setting.getModelName());
        String response = restTemplate.postForObject(setting.getEndpointUrl(), entity, String.class);
        
        if (response == null) {
            throw new RuntimeException("Empty response from LLM");
        }
        
        return parseLlmResponse(response);
    }

    private Map<String, Object> parseLlmResponse(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        JsonNode choices = root.path("choices");
        if (choices.isMissingNode() || choices.size() == 0) {
            log.error("Invalid LLM response: {}", response);
            throw new RuntimeException("No choices returned from LLM");
        }
        
        String jsonContent = choices.get(0).path("message").path("content").asText();
        
        if (jsonContent == null || jsonContent.isEmpty()) {
            throw new RuntimeException("Empty content from LLM choice");
        }
        
        String cleanedJson = jsonContent;
        if (cleanedJson.contains("```json")) {
            cleanedJson = cleanedJson.substring(cleanedJson.indexOf("```json") + 7);
            cleanedJson = cleanedJson.substring(0, cleanedJson.lastIndexOf("```"));
        } else if (cleanedJson.contains("```")) {
            cleanedJson = cleanedJson.substring(cleanedJson.indexOf("```") + 3);
            cleanedJson = cleanedJson.substring(0, cleanedJson.lastIndexOf("```"));
        }
        
        cleanedJson = cleanedJson.trim();
        log.info("Cleaned JSON from LLM: {}", cleanedJson);
        return objectMapper.readValue(cleanedJson, new TypeReference<Map<String, Object>>(){});
    }

    private Map<String, Object> generateMockData(String fileName) {
        Map<String, Object> data = new HashMap<>();
        data.put("contractNo", "AI-" + System.currentTimeMillis() % 10000);
        data.put("contractName", "Extracted: " + fileName);
        data.put("partyAName", "AI Mock Party A");
        data.put("partyBName", "AI Mock Party B");
        data.put("contractAmount", 50000.00);
        data.put("contractType", "Standard");
        return data;
    }
}
