package pl.szymanczyk.peoplemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;

import java.time.LocalDate;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {

    boolean existsByEmployeeAndEndDateAfterAndStartDateBefore(
            Employee employee,
            LocalDate endDate,
            LocalDate startDate
    );
}

