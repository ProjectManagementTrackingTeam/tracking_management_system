package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Employee;
import com.team.tracking_management_system_backend.entity.TaskEmployee;
import com.team.tracking_management_system_backend.repository.TaskEmployeeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Data
@Transactional
public class TaskEmployeeService {
    @Autowired
    TaskEmployeeRepository taskEmployeeRepository;

    public List<TaskEmployee> getEmployeeList(int taskId) {
        return taskEmployeeRepository.getEmployeeList(taskId);
    }

    public void deleteWorker(List<Integer> taskEmpIds) {
        taskEmployeeRepository.deleteWorker(taskEmpIds);
    }

    public void addWorker(List<TaskEmployee> taskEmployees) {
        taskEmployeeRepository.deleteTEs(taskEmployees.get(0).getTask().getId());
        taskEmployeeRepository.saveAll(taskEmployees);

    }
}
