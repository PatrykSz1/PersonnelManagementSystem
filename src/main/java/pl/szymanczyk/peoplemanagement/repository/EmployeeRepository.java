package pl.szymanczyk.peoplemanagement.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Person> findAll(Specification<Person> employeeSpecification, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Person p WHERE TYPE(p) = Employee")
    Page<Person> findAllEmployee(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Employee> findWithLockingById(long id);
}
