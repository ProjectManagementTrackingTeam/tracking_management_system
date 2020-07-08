package com.team.tracking_management_system_backend.controller;

import com.team.tracking_management_system_backend.entity.*;
import com.team.tracking_management_system_backend.service.*;
import com.team.tracking_management_system_backend.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskEmployeeService taskEmployeeService;

    //添加项目同时要为项目添加task
    @PostMapping("addProject")
    public Map addProject(@RequestBody Project project) {
        int managerId = project.getManager().getId();
        //一个项目经理下创建的项目不能重名
        Project oldProject = projectService.findProjectByName(managerId, project.getName());
        if (oldProject != null) {
            return Map.of("message", new MessageVO("该项目已存在"), "projects", projectService.getProjects(managerId));
        }
        projectService.addProject(project);
        return Map.of("message", new MessageVO("创建成功"), "projects", projectService.getProjects(managerId));
    }

    //查看项目
    @PostMapping("listProjects")
    public Map listProject(@RequestBody Manager manager) {
        List<Project> projects = projectService.getProjects(manager.getId());
        return Map.of("projects", projects);
    }

    //删除项目
    @PostMapping("deleteProject")
    public Map deleteProject(@RequestBody Manager manager) {
        List<Project> oldProjects = projectService.getProjects(manager.getId());
        if (oldProjects == null || oldProjects.size() == 0) {
            return Map.of("message", new MessageVO("没有可删除的项目"));
        } else {
            projectService.deleteProjects(manager.
                    getProjects()
                    .stream()
                    .map(Project::getId).collect(Collectors.toList()));
            return Map.of("projects", projectService.getProjects(manager.getId()));
        }
    }

    //项目转让
    @PostMapping("transferProject")
    public Map transferProject(@RequestBody Manager manager) {
        List<Project> oldProjects = projectService.getProjects(manager.getId());
        if (oldProjects == null || oldProjects.size() == 0) {
            return Map.of("message", new MessageVO("没有可转让的项目"));
        } else {
            projectService.transferProject(manager.getProjects()
                            .stream()
                            .map(Project::getId)
                            .collect(Collectors.toList())
                    , manager.getId()
            );
            return Map.of("projects", projectService.getProjects(manager.getId()));
        }
    }

    //添加任务
    @PostMapping("addTasks")
    public Map addTasks(@RequestBody Project project) {
        Project oldProject = projectService.findProjectById(project.getId());
        if (oldProject != null) {
            return Map.of("message", new MessageVO("添加失败，项目不存在"));
        } else {
            projectService.addTasks(project.getTasks(), oldProject);
            return Map.of("message", new MessageVO("添加成功"), "tasks", taskService.getTasks(oldProject.getId()));
        }
    }
    //删除任务
    @PostMapping("deleteTasks")
    public Map deleteTasks(@RequestBody Project project) {
        List<Task> tasks = project.getTasks();
        taskService.deleteTasks(tasks.stream().map(Task::getId).collect(Collectors.toList()));
        return Map.of("tasks", taskService.getTasks(project.getId()));
    }
    //修改任务信息
    @PostMapping("updateTasks")
    public Map updateTasks(@RequestBody Task task){
        Task oldTask = taskService.getTaskById(task.getId());
        oldTask.setStartTime(task.getStartTime());
        oldTask.setEndTime(task.getEndTime());
        oldTask.setIsFinished(task.getIsFinished());
        taskService.addTask(task);
        return Map.of("task",taskService.getTaskById(task.getId()));
    }

    //查看经理管理的所有员工
    @PostMapping("getEmployee")
    public Map getEmployee(@RequestBody Manager manager) {
        List<Project> projects = projectService.getProjects(manager.getId());
        if (projects == null) {
            return Map.of("employee", List.of());
        } else {
            List<Employee> employeeList = employeeService.getEmployeeList(
                    projects.stream()
                            .map(Project::getId)
                            .collect(Collectors.toList())
            );
            return Map.of("employee", employeeList);
        }
    }

    //查看该项目所有可以被分配的员工
    //一个员工只能在一个项目里，所以已经有别的项目的员工不会被查询到
    @PostMapping("getAvailable")
    public Map getAvailable(@RequestBody Manager manager) {
        List<Project> projects = projectService.getProjects(manager.getId());
        List<Integer> collect;
        //todo 这里可能会有问题
        if (projects == null || projects.size() == 0) {
            collect = List.of();
        } else {
            collect = projects.stream()
                    .map(Project::getId)
                    .collect(Collectors.toList());
        }
        return Map.of("employee", employeeService.getAvailable(collect));
    }

    //添加员工,没有给他们项目和任务
    @PostMapping("addEmployee")
    public Map addEmployee(@RequestBody List<Employee> employeeList) {
        employeeService.addEmployee(employeeList);
        return Map.of();
    }

    //为任务添加员工
    @PostMapping("addWorker")
    public Map addWorker(@RequestBody Task task) {
        Task taskById = taskService.getTaskById(task.getId());
        if (taskById == null) {
            return Map.of("message", new MessageVO("任务不存在"));
        } else {
            List<TaskEmployee> oldEmployeeList = taskEmployeeService.getEmployeeList(task.getId());
            oldEmployeeList.addAll(task.getTaskEmployees());
            taskById.setTaskEmployees(oldEmployeeList);
            taskService.addWorker(taskById);
            return Map.of("TaskEmployee",taskEmployeeService.getEmployeeList(taskById.getId()));
        }
    }
    //为任务删除员工
    @PostMapping
    public Map deleteWorker(@RequestBody Task task){
        Task taskById = taskService.getTaskById(task.getId());
        if (taskById == null) {
            return Map.of("message", new MessageVO("任务不存在"));
        }else {
            List<TaskEmployee> taskEmployees = task.getTaskEmployees();
            List<Integer> taskEmpIds = taskEmployees.stream().map(TaskEmployee::getId).collect(Collectors.toList());
            taskEmployeeService.deleteWorker(taskEmpIds);
            return Map.of("TaskEmployee",taskEmployeeService.getEmployeeList(taskById.getId()));
        }
    }
}
