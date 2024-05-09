package pl.szymanczyk.peoplemanagement.model.pensioner;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.szymanczyk.peoplemanagement.model.PersonDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PensionerDto extends PersonDto {

    @Min(value = 1, message = "Amount has to be min 1")
    @NotNull(message = "Pension can not be null")
    private Integer pension;

    @Min(value = 1, message = "Years of work has to be min 1")
    @NotNull(message = "Employment years can not be null")
    private Integer employmentYears;

}
