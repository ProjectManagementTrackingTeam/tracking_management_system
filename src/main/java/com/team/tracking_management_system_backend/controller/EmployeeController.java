package com.team.tracking_management_system_backend.controller;

import com.team.tracking_management_system_backend.component.RequestComponent;
import com.team.tracking_management_system_backend.entity.Employee;
import com.team.tracking_management_system_backend.entity.TaskEmployee;
import com.team.tracking_management_system_backend.service.EmployeeService;
import com.team.tracking_management_system_backend.service.TaskEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private RequestComponent requestComponent;
    @Autowired
    private TaskEmployeeService taskEmployeeService;
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/getTasks")
    public Map getTasks(){
        int uid = requestComponent.getUid();
        List<TaskEmployee> tasksFromEmp = taskEmployeeService.getTasksFromEmp(uid);
        return Map.of("MyTasks",tasksFromEmp);
    }
    @GetMapping("employeeindex")
    public Map getEmplyee(){
        Employee employee = employeeService.getEmplyee(requestComponent.getUid());
        return Map.of(
                "employee",employee
        );
    }

}
