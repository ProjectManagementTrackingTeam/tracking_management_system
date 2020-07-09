package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Employee;
import com.team.tracking_management_system_backend.entity.Task;
import com.team.tracking_management_system_backend.entity.TaskEmployee;
import com.team.tracking_management_system_backend.repository.EmployeeRepository;
import com.team.tracking_management_system_backend.repository.TaskEmployeeRepository;
import com.team.tracking_management_system_backend.repository.TaskRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@Transactional
public class TaskEmployeeService {
    @Autowired
    TaskEmployeeRepository taskEmployeeRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EmployeeRepository employeeRepository;


    public List<TaskEmployee> getEmployeeList(int taskId) {
        List<TaskEmployee> employeeList = taskEmployeeRepository.getEmployeeList(taskId);
        if (employeeList == null){
            return List.of();
        }
        return employeeList;
    }

    public void deleteWorker(List<Integer> taskEmpIds) {
        taskEmployeeRepository.deleteWorker(taskEmpIds);
    }

    public void addWorker(List<TaskEmployee> taskEmployees) {
        int taskId = taskEmployees.get(0).getTask().getId();
        taskEmployeeRepository.deleteTEs(taskId);
        Task one = taskRepository.getOne(taskId);
        taskEmployeeRepository.saveAll(taskEmployees.stream()
                .map(e -> {
                    e.setTask(one);
                    return e;
                }).collect(Collectors.toList()));

    }
}
