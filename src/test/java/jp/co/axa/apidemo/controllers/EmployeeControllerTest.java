package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;
import org.assertj.core.internal.bytebuddy.matcher.ElementMatchers;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    EmployeeService employeeService;

    Employee emp1 = new Employee( 1L,"John Smith", 5000, "IT");
    Employee emp2 = new Employee( 2L,"Adam swill", 5000, "HR");
    Employee emp3 = new Employee( 3L,"Michael Jude", 5000, "Finance");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllEmployees_success() throws Exception {
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
        Mockito.when(employeeService.retrieveEmployees()).thenReturn(employeeList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.is("Michael Jude")));
    }

    @Test
    void getEmployeeById_success() throws Exception {
        Mockito.when(employeeService.getEmployee(emp1.getId())).thenReturn(emp1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("John Smith")));
    }

    @Test
    void saveEmployee_success() throws Exception {
        Employee newEmployee = new Employee( 4L, "John Doe", 4500, "Sales");

        Mockito.when(employeeService.saveEmployee(newEmployee)).thenReturn(newEmployee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEmployee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void deleteEmployee_success() throws Exception {
        Mockito.when(employeeService.deleteEmployee(emp2.getId())).thenReturn(emp2.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employees/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmployee_success() throws Exception {
        Employee updateEmployee = new Employee( 3L, "John Doe", 5500, "Sales");

        Mockito.when(employeeService.saveEmployee(updateEmployee)).thenReturn(updateEmployee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateEmployee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
}