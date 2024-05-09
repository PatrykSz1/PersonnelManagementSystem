package pl.szymanczyk.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import pl.szymanczyk.peoplemanagement.model.student.Student;
import pl.szymanczyk.peoplemanagement.model.student.StudentDto;

import java.util.Map;

@Mapper
public interface StudentMapper {

    Student mapToEntity(Map<String, String> args);

    StudentDto mapToDto(Map<String, String> args);
}