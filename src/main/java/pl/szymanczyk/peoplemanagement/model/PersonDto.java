package pl.szymanczyk.peoplemanagement.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PersonDto {

    private int id;

    @NotNull(message = "First Name can not be null")
    @Pattern(regexp = "[A-ZŁŻ][a-ząćęłńóśźż]+", message = "Invalid first name format. The name should start with an uppercase letter followed by lowercase letters.")
    private String firstName;

    @NotNull(message = "Last Name can not be null")
    @Pattern(regexp = "[A-ZŁŻ][a-ząćęłńóśźż]+", message = "Invalid last name format. The name should start with an uppercase letter followed by lowercase letters.")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String mail;

    @NotNull(message = "Pesel can not be null")
    @Column(unique = true)
    @Size(min = 11, max = 11, message = "Pesel must have 11 digits")
    private String personalId;

    @Min(value = 20, message = "Height has to be at least 20 cm")
    @Max(value = 300, message = "Height cannot be more than 300 cm")
    private Integer height;

    @Min(value = 1, message = "Weight has to be at least 1 kg")
    @Max(value = 200, message = "Weight cannot be more than 200kg")
    private Integer weight;

    private Role role;

    @NotNull(message = "Password can not be null")
    private String password;

    private boolean blocked = false;
}
