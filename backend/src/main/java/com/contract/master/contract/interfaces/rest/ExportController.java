package com.contract.master.contract.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.application.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/contracts")
    public void exportContracts(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=contracts.csv");
        exportService.exportToCsv(response.getWriter());
    }

    @GetMapping("/contracts/pdf")
    public void exportPdf(@RequestParam String id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=contract.pdf");
        response.getOutputStream().write(exportService.exportToPdf(id));
    }
}
