package com.example.employee.service;

import com.example.employee.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee  createEmployee(Employee employee);

    Optional<Employee> getEmployeesDetailsById(Long id);

    List<Employee> getAllEmployeesDetails();

    Employee updateEmployeeDetails(Long id, Employee employee);

    void deleteEmployeeById(Long id);

    Employee deactivateEmployee(Long id);
}
