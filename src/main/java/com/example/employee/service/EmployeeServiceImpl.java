package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if(Optional.ofNullable(employeeRepository.findByEmail(employee.getEmail())).isPresent()){
            throw new RuntimeException("Employee already present.");
        }else{
            return employeeRepository.save(employee);
        }
    }

    @Override
    public Optional<Employee> getEmployeesDetailsById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployeesDetails() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployeeDetails(Long id, Employee employee) {
        Employee emp = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Details not found!!"));
        emp.setFirstName(employee.getFirstName());
        emp.setLastName(employee.getLastName());
        emp.setEmail(employee.getEmail());
        emp.setDepartment(employee.getDepartment());
        emp.setSalary(employee.getSalary());
        emp.setHireDate(employee.getHireDate());
        emp.setActive(employee.isActive());
        return employeeRepository.save(emp);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        Employee emp = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee Details not found!!"));
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee deactivateEmployee(Long id) {
        Employee emp = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee details not found!!"));
        emp.setActive(false);
        return employeeRepository.save(emp);
    }
}
