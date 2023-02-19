package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.ResourceNotFoundException;

import java.util.List;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public Employee saveEmployee(Employee employee);

    public Long deleteEmployee(Long employeeId) throws ResourceNotFoundException;

    public Employee updateEmployee(Employee employee);
}