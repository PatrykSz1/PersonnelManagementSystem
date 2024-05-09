package pl.szymanczyk.peoplemanagement.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.student.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Person> findAll(Specification<Person> studentSpecification, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Person p WHERE TYPE(p) = Student")
    Page<Person> findAllStudent(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Student> findWithLockingById(Long id);

}