package pl.szymanczyk.peoplemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.szymanczyk.peoplemanagement.model.ImportStatus;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.pensioner.Pensioner;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Component
@Service
@RequiredArgsConstructor
public class CsvImportService {

    private static final String csvFileName = "C:\\Users\\PC\\Desktop\\delete\\myfile.csv";
    private static final int BUFFER_SIZE = 8192;
    private final PersonRepository personRepository;
    private final Lock importLock = new ReentrantLock();

    @Async
    public CompletableFuture<ImportStatus> importCsv(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            ImportStatus importStatus = new ImportStatus();
            importLock.lock();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                while (reader.readLine() != null) {
                    importStatus.incrementProcessedRows();
                }
                importStatus.setCreatedAt(LocalDateTime.now());
                importStatus.setStartTime(LocalDateTime.now());
                importStatus.setStatus(ImportStatus.Status.COMPLETED);
            } catch (Exception e) {
                importStatus.setStatus(ImportStatus.Status.FAILED);
            } finally {
                importLock.unlock();
            }

            return importStatus;
        });
    }

    private void writeHeader(PrintWriter pw) {
        pw.println("TYPE, first name, last name, personalId, height, weight, address email");
    }

    private void writePersonData(PrintWriter pw, Person person) {
        pw.println(convertToCSV(getPersonData(person)));
    }

    public String generateCsv() throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csvFileName, true), BUFFER_SIZE))) {
            writeHeader(pw);

            personRepository.findAll()
                    .forEach(person -> writePersonData(pw, person));

            return "File CSV is saved.";
        } catch (IOException e) {
            throw new RuntimeException("Error during CSV file generation.", e);
        }
    }

    private String[] getPersonData(Person person) {
        return new String[]{
                getPersonType(person),
                person.getFirstName() + " " + person.getLastName(),
                person.getPersonalId() + " " + person.getHeight(),
                String.valueOf(person.getWeight()),
                person.getMail()
        };
    }

    private String convertToCSV(String[] data) {
        return String.join(",", escapeSpecialCharacters(data));
    }

    private String[] escapeSpecialCharacters(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .toArray(String[]::new);
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");

        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }

        return escapedData;
    }

    private String getPersonType(Person person) {
        return (person instanceof Employee) ? "Employee" :
                (person instanceof Pensioner) ? "Pensioner" :
                        (person instanceof Student) ? "Student" : "Person";
    }
}

