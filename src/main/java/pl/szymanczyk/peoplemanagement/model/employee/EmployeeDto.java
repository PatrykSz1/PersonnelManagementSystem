package pl.szymanczyk.peoplemanagement.model.employee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.szymanczyk.peoplemanagement.model.PersonDto;
import pl.szymanczyk.peoplemanagement.model.Role;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class EmployeeDto extends PersonDto {

    @NotNull(message = "Start Date of employment can not be null")
    @PastOrPresent(message = "It has to be current employment")
    private LocalDate startDateOfEmployment;

    @NotNull(message = "Position can not be null")
    @Pattern(regexp = "^[A-ZŁŻ][a-zA-Ząćęłńóśźż\\s]+", message = "Invalid position format. The name should start with an uppercase letter followed by letters and spaces.")
    private String position;

    @NotNull(message = "Salary can not be null")
    @Min(value = 1, message = "Value cannot be less than 1!")
    private BigDecimal salary;

    private Role role;
}

