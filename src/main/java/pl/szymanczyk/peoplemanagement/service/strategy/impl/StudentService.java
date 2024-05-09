package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import pl.szymanczyk.peoplemanagement.mapper.StudentMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.model.student.StudentDto;
import pl.szymanczyk.peoplemanagement.repository.StudentRepository;
import pl.szymanczyk.peoplemanagement.service.strategy.PersonStrategy;

@Service("STUDENT")
@RequiredArgsConstructor
@Slf4j
public class StudentService implements PersonStrategy {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Page<Person> findAllByFilter(PersonFilter filter, Pageable pageable) {
        if (filter != null) {
            return studentRepository.findAll(filter.buildSpecification(), pageable);
        } else {
            return studentRepository.findAllStudent(pageable);
        }
    }

    @Override
    public Person save(PersonRequestDto personRequestDto) {
        return studentRepository.save(studentMapper.mapToEntity(personRequestDto.getArgs()));
    }

    @Override
    @Transactional
    @Modifying
    public Person update(Long personId, PersonRequestDto updatedPersonData) {
        Student student = studentRepository.findWithLockingById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + personId));
        updateStudentData(student, updatedPersonData);
        return studentRepository.save(student);
    }

    private void updateStudentData(Student student, PersonRequestDto updatedPersonData) {
        StudentDto studentDto = studentMapper.mapToDto(updatedPersonData.getArgs());
        student.setAcademyName(studentDto.getAcademyName());
        student.setStudyYear(studentDto.getStudyYear());
        student.setStudyField(studentDto.getStudyField());
        student.setScholarship(studentDto.getScholarship());
    }
}
