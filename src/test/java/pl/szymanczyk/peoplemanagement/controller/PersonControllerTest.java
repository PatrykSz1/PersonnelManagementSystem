package pl.szymanczyk.peoplemanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szymanczyk.peoplemanagement.model.PersonRequestDto;
import pl.szymanczyk.peoplemanagement.model.Role;
import pl.szymanczyk.peoplemanagement.model.employee.Employee;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePosition;
import pl.szymanczyk.peoplemanagement.model.employeeposition.EmployeePositionDto;
import pl.szymanczyk.peoplemanagement.repository.EmployeePositionRepository;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.szymanczyk.peoplemanagement.model.Role.EMPLOYEE;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmployeePositionRepository employeePositionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Employee employee = Employee.builder()
                .id(1L)
                .mail("employee12345@example.com")
                .personalId("22422222222")
                .firstName("John")
                .lastName("Doe")
                .height(180)
                .weight(80)
                .role(EMPLOYEE)
                .password("password")
                .blocked(false)
                .startDateOfEmployment(LocalDate.now())
                .position("Position")
                .salary(BigDecimal.valueOf(5000))
                .build();

        personRepository.save(employee);
        EmployeePosition employeePosition = EmployeePosition.builder()
                .employee(employee)
                .startDate(LocalDate.now())
                .endDate(null)
                .position("Position")
                .salary(BigDecimal.valueOf(5000))
                .build();

        employeePositionRepository.save(employeePosition);
    }

    @BeforeEach
    void clean() {
        personRepository.deleteAll();
    }

    @Test
    void testFindAllByFilter_ShouldReturnListOfPeople() throws Exception {
        mockMvc.perform(get("/api/v1/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id", equalTo(1)))
                .andExpect(jsonPath("$.content[0].firstName", equalTo("John")))
                .andExpect(jsonPath("$.content[0].lastName", equalTo("Doe")))
                .andExpect(jsonPath("$.content[0].mail", equalTo("employee1@example.com")))
                .andExpect(jsonPath("$.content[0].personalId", equalTo("1234567890")))
                .andExpect(jsonPath("$.content[0].height", equalTo(180)))
                .andExpect(jsonPath("$.content[0].weight", equalTo(80)))
                .andExpect(jsonPath("$.content[0].role", equalTo("EMPLOYEE")));
    }

    @Test
    @WithMockUser(username = "employee", roles = {"EMPLOYEE"})
    public void testAddPositionToEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/people/1/positions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.position").value("Position"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateEmployeePosition() throws Exception {
        EmployeePositionDto employeePositionDto = EmployeePositionDto.builder()
                .id(1)
                .startDate(LocalDate.ofEpochDay(2022 - 2 - 22))
                .endDate(LocalDate.ofEpochDay(2023 - 2 - 22))
                .position("Position")
                .salary(BigDecimal.valueOf(5000))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/people/create-employee-position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeePositionDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(employeePositionDto.getStartDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(employeePositionDto.getEndDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(employeePositionDto.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(employeePositionDto.getSalary().doubleValue()));
    }

    @Test
    public void testSavePerson() throws Exception {
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("student");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("firstName", "John");
        personRequestDto.getArgs().put("lastName", "Doe");
        personRequestDto.getArgs().put("mail", "student@example.com");
        personRequestDto.getArgs().put("personalId", "12345678901");
        personRequestDto.getArgs().put("height", "180");
        personRequestDto.getArgs().put("weight", "70");
        personRequestDto.getArgs().put("role", Role.STUDENT.toString());
        personRequestDto.getArgs().put("password", "password");
        personRequestDto.getArgs().put("academyName", "Some Academy");
        personRequestDto.getArgs().put("studyYear", "2");
        personRequestDto.getArgs().put("studyField", "Computer Science");
        personRequestDto.getArgs().put("scholarship", "1500");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.mail").value("student@example.com"))
                .andExpect(jsonPath("$.personalId").value("12345678901"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdatePerson() throws Exception {
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setType("employee");
        personRequestDto.setArgs(new HashMap<>());
        personRequestDto.getArgs().put("update", "UpdatedPosition");
        personRequestDto.getArgs().put("mail", "updated_mail@example.com");
        personRequestDto.getArgs().put("personalId", "updated_personal_id");
        personRequestDto.getArgs().put("height", "185");
        personRequestDto.getArgs().put("weight", "75");
        personRequestDto.getArgs().put("role", Role.EMPLOYEE.toString());
        personRequestDto.getArgs().put("password", "updated_password");
        personRequestDto.getArgs().put("blocked", "true");
        personRequestDto.getArgs().put("startDateOfEmployment", "2024-02-26");
        personRequestDto.getArgs().put("position", "UpdatedPosition");
        personRequestDto.getArgs().put("salary", "6000");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
