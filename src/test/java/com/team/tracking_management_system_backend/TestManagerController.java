package com.team.tracking_management_system_backend;

import com.team.tracking_management_system_backend.entity.*;
import com.team.tracking_management_system_backend.repository.*;
import com.team.tracking_management_system_backend.service.EmployeeService;
import com.team.tracking_management_system_backend.service.ProjectService;
import com.team.tracking_management_system_backend.service.TaskEmployeeService;
import com.team.tracking_management_system_backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Rollback(value = false)
@Transactional
public class TestManagerController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    TaskEmployeeService taskEmployeeService;
    @Autowired
    TaskService taskService;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    TaskEmployeeRepository taskEmployeeRepository;

    //加入几个项目经理
    @Test
    public void addManager() {
        Manager manager = new Manager();
        manager.setUser(new User(User.Role.MANAGER, "man3", 1006));
        Manager manager1 = new Manager();
        manager.setUser(new User(User.Role.MANAGER, "man2", 1006));
        managerRepository.saveAll(List.of(manager, manager1));
    }

    //加入项目
    @Test
    public void addProjects() {
        Project project = new Project();
        project.setManager(new Manager(5));
        projectService.addProject(project);
    }

    //转让项目
    @Test
    public void transferProject() {
        projectService.transferProject(List.of(18, 19), 2);
    }

    //删除项目
    @Test
    public void deleteProject() {
        projectService.deleteProjects(List.of(10, 11));
    }

    //修改项目信息
    @Test
    public void updateProject() {
        Project project = new Project(14);
        project.setName("14");
        project.setStartTime(LocalDateTime.now());
        project.setEndTime(LocalDateTime.now());
        project.setEmergencyDegree(2);
        projectService.updateProject(project);
    }

    //为项目添加任务
    @Test
    public void addTasks() {
        Project project = new Project(14);
        Task task = new Task();
        task.setProject(project);
        Task task1 = new Task();
        task1.setProject(project);
        task.setWeight(2);
        task.setStartTime(LocalDateTime.now());
        project.setTasks(List.of(task1, task));
        projectService.addTasks(project);
    }

    //删除任务
    @Test
    void deleteTask() {
        taskService.deleteTasks(List.of(1));
    }

    //查看所有可用员工
    @Test
    void showAvali() {
        List<Employee> available = employeeService.getAvailable(List.of(12, 14));
        available.stream().map(a -> a.getUser().getName()).forEach(System.out::println);
    }

    //查看一个经理的所有员工
    @Test
    void showAllEmployee() {
        employeeService.getEmployeeList(List.of(15, 17, 18)).stream()
                .map(e -> e.getUser().getName())
                .forEach(System.out::println);
    }



    @Test
    public void addWorker() {
        TaskEmployee taskEmployee = new TaskEmployee();
        Task taskById = taskService.getTaskById(2);
        Employee e = employeeService.getEmployeeById(10);
        Employee e2 = employeeService.getEmployeeById(11);
        taskEmployee.setTask(taskById);
        taskEmployee.setEmployee(e);
        TaskEmployee taskEmployee1 = new TaskEmployee();
        taskEmployee1.setTask(taskById);
        taskEmployee1.setEmployee(e2);
        taskEmployeeService.addWorker(List.of(taskEmployee,taskEmployee1));
    }
    //为任务删除员工
    @Test
    void deleteWorker(){
        taskEmployeeService.deleteWorker(List.of(4,10));
    }

}
