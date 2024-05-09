package pl.szymanczyk.peoplemanagement.exception;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class ValidationExceptionDto extends ExceptionDto {
    private final List<ViolationInfo> violations = new ArrayList<>();

    public ValidationExceptionDto() {
        super("Constraint violations");
    }

    public void addViolations(String field, String message) {
        violations.add(new ViolationInfo(field, message));
    }

    @Value
    public static class ViolationInfo {
        String field;
        String message;
    }
}