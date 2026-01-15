package com.contract.master.api;

import com.contract.master.dto.ContractDTO;
import com.contract.master.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/publish")
public class PublishController {

    @Autowired
    private PublishService publishService;

    @GetMapping("/contracts")
    public List<ContractDTO> publish(@RequestParam String accessKey) {
        return publishService.getPublishedData(accessKey);
    }
}
