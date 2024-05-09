package pl.szymanczyk.peoplemanagement.service.strategy.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.szymanczyk.peoplemanagement.mapper.StudentMapper;
import pl.szymanczyk.peoplemanagement.model.Person;
import pl.szymanczyk.peoplemanagement.model.PersonFilter;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.model.student.StudentDto;
import pl.szymanczyk.peoplemanagement.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testFindAllByFilter_ShouldReturnStudentsList() {
        List<Student> students = new ArrayList<>();
        Student student = Student.builder()
                .id(1L)
                .firstName("test")
                .lastName("testowy")
                .mail("testowy@mail.com")
                .personalId("11111111111")
                .height(180)
                .weight(80)
                .password("password")
                .blocked(false)
                .academyName("university")
                .studyYear(3)
                .build();
        Student student2 = Student.builder()
                .id(2L)
                .firstName("patryk")
                .lastName("patrykowy")
                .mail("patrykowy@mail.com")
                .personalId("222222222")
                .height(180)
                .weight(80)
                .password("password")
                .blocked(false)
                .academyName("university")
                .studyYear(2)
                .build();
        students.add(student);
        students.add(student2);

        PersonFilter filter = new PersonFilter();
        filter.setAcademyName("university");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> page = new PageImpl<>(students);

        when(studentRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Person> resultPage = studentService.findAllByFilter(filter, pageable);
        assertEquals(2, resultPage.getTotalElements());
        assertTrue(resultPage.getContent().contains(student));
        assertTrue(resultPage.getContent().contains(student2));
    }

    @Test
    void findAllByFilter_ShouldReturnAllStudentsWithoutFilter() {
        Student student = Student.builder()
                .id(1L)
                .firstName("test")
                .lastName("testowy")
                .mail("testowy@mail.com")
                .personalId("11111111111")
                .height(180)
                .weight(80)
                .password("password")
                .blocked(false)
                .academyName("university")
                .studyYear(3)
                .build();
        Pageable pageable = Pageable.unpaged();
        List<Student> students = Collections.singletonList(student);

        Page<Student> studentPage = new PageImpl<>(students);

        when(studentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(studentPage);

        Page<Person> result = studentService.findAllByFilter(new PersonFilter(), pageable);

        assertEquals(students.size(), result.getContent().size());
        assertEquals(students, result.getContent());
        verify(studentRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSave_ShouldSaveStudent() {
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("student");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("firstName", "John");
        personRequestDto.getArgs().put("lastName", "Doe");
        personRequestDto.getArgs().put("academyName", "University of Example");
        personRequestDto.getArgs().put("studyYear", "3");

        Student studentToSave = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .academyName("University of Example")
                .studyYear(3)
                .build();

        when(studentMapper.mapToEntity(personRequestDto.getArgs())).thenReturn(studentToSave);
        when(studentRepository.save(studentToSave)).thenReturn(studentToSave);

        Person savedPerson = studentService.save(personRequestDto);

        assertNotNull(savedPerson);
        assertEquals(studentToSave, savedPerson);
        verify(studentRepository, times(1)).save(studentToSave);
    }

    @Test
    void testUpdate_ShouldUpdatePensioner() {
        Long personId = 1L;
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("student");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("firstName", "John");
        personRequestDto.getArgs().put("lastName", "Doe");
        personRequestDto.getArgs().put("academyName", "University of Example");
        personRequestDto.getArgs().put("studyYear", "3");


        Student studentToSave = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .academyName("University of Example")
                .studyYear(3)
                .build();

        StudentDto studentDto = new StudentDto();
        studentDto.setStudyYear(5);
        when(studentMapper.mapToDto(personRequestDto.getArgs())).thenReturn(studentDto);

        when(studentRepository.findWithLockingById(personId)).thenReturn(Optional.of(studentToSave));
        when(studentRepository.save(studentToSave)).thenReturn(studentToSave);

        Student updateStudent = (Student) studentService.update(personId, personRequestDto);

        assertNotNull(updateStudent);
        assertEquals(5, updateStudent.getStudyYear());

        verify(studentRepository, times(1)).findWithLockingById(personId);
        verify(studentRepository, times(1)).save(studentToSave);
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundExceptionWithCorrectMessage() {
        Long personId = 1L;
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("student");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("firstName", "John");
        personRequestDto.getArgs().put("lastName", "Doe");
        personRequestDto.getArgs().put("academyName", "University of Example");
        personRequestDto.getArgs().put("studyYear", "3");

        when(studentRepository.findWithLockingById(personId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            studentService.update(personId, personRequestDto);
        });

        verify(studentRepository, times(1)).findWithLockingById(personId);
        verify(studentRepository, never()).save(any());

        assertEquals("Student not found with ID: " + personId, exception.getMessage());
    }
}