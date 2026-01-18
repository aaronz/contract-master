package com.contract.master.ai.application;

import com.contract.master.contract.domain.model.ContractAttachment;
import com.contract.master.contract.domain.repository.ContractAttachmentRepository;
import com.contract.master.contract.dto.ContractDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AIService {

    @Autowired
    private ContractAttachmentRepository attachmentRepository;

    public String analyzeWithPrompt(ContractDTO contract, String promptTemplate) {
        return "AI Analysis Result: NO VIOLATION";
    }

    public Map<String, Object> extractContractData(String contractId, MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        ContractAttachment attachment = new ContractAttachment();
        attachment.setAttachmentId(fileId);
        attachment.setContractId(contractId);
        attachment.setAttachmentName(file.getOriginalFilename());
        attachment.setStoragePath("/storage/" + fileId);
        attachmentRepository.save(attachment);

        Map<String, Object> extracted = new HashMap<>();
        extracted.put("payment_term", "30 days after delivery");
        extracted.put("liquidated_damages", "0.5% per day");
        extracted.put("warranty_period", "24 months");
        extracted.put("fill_type", "AI");
        extracted.put("verification_status", "UNVERIFIED");
        
        return extracted;
    }
}
