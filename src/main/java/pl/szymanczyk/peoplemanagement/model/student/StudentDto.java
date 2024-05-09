package pl.szymanczyk.peoplemanagement.model.student;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.szymanczyk.peoplemanagement.model.PersonDto;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class StudentDto extends PersonDto {

    @Pattern(regexp = "^[A-ZŁŻ][a-zA-Ząćęłńóśźż\\s]+", message = "Invalid academy Name format. The name should start with an uppercase letter followed by letters and spaces.")
    @NotNull(message = "Academy name can not be null")
    private String academyName;

    @NotNull(message = "Study Year can not be null")
    @Min(value = 1, message = "Study year has to be min 1")
    private Integer studyYear;

    @NotNull(message = "StudyField can not be null")
    @Pattern(regexp = "^[A-ZŁŻ][a-zA-Ząćęłńóśźż\\s]+", message = "Invalid study field format. The study field should start with an uppercase letter followed by letters and spaces.")
    private String studyField;

    @NotNull(message = "Scholarship can not be null")
    @Min(value = 1, message = "Scholarship amount has to be min 1")
    private BigDecimal scholarship;
}
