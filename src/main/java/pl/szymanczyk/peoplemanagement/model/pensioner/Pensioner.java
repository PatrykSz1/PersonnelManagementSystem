package pl.szymanczyk.peoplemanagement.model.pensioner;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.szymanczyk.peoplemanagement.model.Person;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Pensioner extends Person {

    private Integer pension;
    private Integer employmentYears;
}