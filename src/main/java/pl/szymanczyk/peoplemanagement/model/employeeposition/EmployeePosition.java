package pl.szymanczyk.peoplemanagement.model.employeeposition;

import jakarta.persistence.*;
import lombok.*;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class EmployeePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String position;
    private BigDecimal salary;
}
