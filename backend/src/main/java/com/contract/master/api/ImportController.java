package com.contract.master.api;

import com.contract.master.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    @Autowired
    private ImportService importService;

    @PostMapping("/contracts")
    public void importContracts(@RequestParam("file") MultipartFile file) throws Exception {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));
            }
        }
        importService.importFromCsv(rows);
    }
}
