package pl.szymanczyk.peoplemanagement.model;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"),
    IMPORTER("Importer"),
    STUDENT("Student"),
    PENSIONER("Pensioner"),
    EMPLOYEE("Employee");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
