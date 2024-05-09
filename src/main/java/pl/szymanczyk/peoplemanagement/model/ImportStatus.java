package pl.szymanczyk.peoplemanagement.model;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportStatus {

    public enum Status {
        IN_PROGRESS, COMPLETED, FAILED
    }

    private Status status = Status.IN_PROGRESS;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private int processedRows;

    public synchronized void incrementProcessedRows() {
        processedRows++;
    }
}
