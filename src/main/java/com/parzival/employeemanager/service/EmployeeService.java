package com.parzival.employeemanager.service;

import com.parzival.employeemanager.exception.UserNotFoundException;
import com.parzival.employeemanager.model.Employee;
import com.parzival.employeemanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(Employee employee) {
        employee.setEmployeeCode(UUID.randomUUID().toString());
        return employeeRepository.save(employee);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Long employeeId, Employee newEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + employeeId + " does not exists"
                ));
        if (newEmployee.getName() != null &&
                newEmployee.getName().length() > 0 &&
                !Objects.equals(employee.getName(), newEmployee.getName())) {
            employee.setName(newEmployee.getName());
        }
        if (newEmployee.getEmail() != null &&
                newEmployee.getEmail().length() > 0 &&
                !Objects.equals(employee.getEmail(), newEmployee.getEmail())) {
            Optional<Employee> employeeOptional = employeeRepository
                    .findByEmail(newEmployee.getEmail());
            if (employeeOptional.isPresent()) {
                throw new IllegalStateException("email already taken");
            }
            employee.setEmail(newEmployee.getEmail());
        }
        return employee;
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findEmployeeById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " not found"));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteEmployeeById(id);
    }
}
