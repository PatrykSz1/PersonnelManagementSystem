package pl.szymanczyk.peoplemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.szymanczyk.peoplemanagement.model.ImportStatus;
import pl.szymanczyk.peoplemanagement.service.CsvImportService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/csv")
@RequiredArgsConstructor
public class CsvController {

    private final CsvImportService csvImportService;

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv() {
        try {
            String result = csvImportService.generateCsv();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error during export CSV file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN') or hasRole('IMPORTER')")
    public ResponseEntity<Object> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            CompletableFuture<ImportStatus> importStatusFuture = csvImportService.importCsv(file);
            ImportStatus importStatus = importStatusFuture.get();
            return new ResponseEntity<>(importStatus, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
