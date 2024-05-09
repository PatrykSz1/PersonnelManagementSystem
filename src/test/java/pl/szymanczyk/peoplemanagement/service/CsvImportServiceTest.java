package pl.szymanczyk.peoplemanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.szymanczyk.peoplemanagement.model.ImportStatus;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CsvImportService csvImportService;

//    @Test
//    void importCsv_ValidFile_ShouldImportData() throws IOException {
//        String csvData = "John,Doe\nJane,Smith\nAdam,Johnson";
//        byte[] csvBytes = csvData.getBytes(StandardCharsets.UTF_8);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(csvBytes);
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "myfile.csv", "text/csv", byteArrayInputStream);
//
//        CompletableFuture<ImportStatus> future = csvImportService.importCsv(multipartFile);
//
//        assertDoesNotThrow(() -> future.get());
//        assertTrue(future.isDone());
//        assertEquals(ImportStatus.Status.COMPLETED, future.join().getStatus());
//    }

    @Test
    public void testImportCsv_Success() throws Exception {
        String csvData = "row1\nrow2\nrow3";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        MultipartFile multipartFile = new MockMultipartFile("file.csv", inputStream);

        ImportStatus importStatus = csvImportService.importCsv(multipartFile).get();

        assertEquals(3, importStatus.getProcessedRows());
        assertEquals(ImportStatus.Status.COMPLETED, importStatus.getStatus());
        assertEquals(importStatus.getCreatedAt(), importStatus.getStartTime());
    }

    @Test
    public void testImportCsv_Failure() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(new RuntimeException("Test Exception"));

        ImportStatus importStatus = csvImportService.importCsv(multipartFile).get();

        assertEquals(0, importStatus.getProcessedRows());
        assertEquals(ImportStatus.Status.FAILED, importStatus.getStatus());
    }

    @Test
    public void testGenerateCsv_Success() throws Exception {
        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setPersonalId("123456789");
        employee1.setHeight(180);
        employee1.setWeight(80);
        employee1.setMail("john.doe@example.com");

        when(personRepository.findAll()).thenReturn(java.util.Arrays.asList(employee1));

        String result = csvImportService.generateCsv();

        assertEquals("File CSV is saved.", result);
    }
}