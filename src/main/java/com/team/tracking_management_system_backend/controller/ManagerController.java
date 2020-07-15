package com.team.tracking_management_system_backend.controller;

import com.team.tracking_management_system_backend.component.RequestComponent;
import com.team.tracking_management_system_backend.entity.*;
import com.team.tracking_management_system_backend.service.*;
import com.team.tracking_management_system_backend.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskEmployeeService taskEmployeeService;
    @Autowired
    private RequestComponent requestComponent;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UserService userService;

    //添加项目同时要为项目添加task
    @PostMapping("addProject")
    public Map addProject(@RequestBody Project project) {
        int managerId = requestComponent.getUid();
        //一个项目经理下创建的项目不能重名
        Project oldProject = projectService.findProjectByName(managerId, project.getName());
        if (oldProject != null) {
            return Map.of("message", new MessageVO("该项目已存在"), "projects", projectService.getProjects(managerId));
        }
        project.setManager(new Manager(managerId));
        projectService.addProject(project);
        return Map.of("message", new MessageVO("创建成功"), "projects", projectService.getProjects(managerId));
    }

    //查看项目
    @GetMapping("listProjects")
    public Map listProject() {
        List<Project> projects = projectService.getProjects(requestComponent.getUid());
        return Map.of("projects", projects);
    }

    //删除项目
    @PostMapping("deleteProject")
    public Map deleteProject(@RequestBody List<Project> projects) {
        List<Project> oldProjects = projectService.getProjects(requestComponent.getUid());
        if (oldProjects == null || oldProjects.size() == 0) {
            return Map.of("message", new MessageVO("没有可删除的项目"));
        } else {
            projectService.deleteProjects(projects
                    .stream()
                    .map(Project::getId).collect(Collectors.toList()));
            return Map.of("projects", projectService.getProjects(requestComponent.getUid()));
        }
    }

    //项目转让
    @PostMapping("transferProject")
    public Map transferProject(@RequestBody Manager manager) {
        System.out.println("projects----------" + manager.getProjects()+"manager" +manager);
        User user = manager.getUser();
        Integer number = user.getNumber();
        String name = user.getName();
        User getUser = userService.getUserByNumberAndName(number, name);
        if (getUser == null){
            return Map.of("message",new MessageVO("用户名或账号错误"));
        }
        List<Project> oldProjects = projectService.getProjects(requestComponent.getUid());
        if (oldProjects == null || oldProjects.size() == 0) {
            return Map.of("message", new MessageVO("没有可转让的项目"));
        } else {
            Manager one = managerService.getManager(manager.getId());
            if(one == null){
                return Map.of("message", new MessageVO("该管理员不存在"));
            }

            projectService.transferProject(manager.getProjects()
                            .stream()
                            .map(Project::getId)
                            .collect(Collectors.toList())
                    , getUser.getId()
            );
            return Map.of("projects", projectService.getProjects(requestComponent.getUid()));
        }
    }
    //修改项目信息
    @PostMapping("updateProject")
    public Map updateProject(@RequestBody Project project){
        if (!projectService.updateProject(project)){
            return Map.of("message", new MessageVO("该项目不存在"));
        }else{
            return Map.of("project",projectService.findProjectById(project.getId()));
        }
    }
    @GetMapping("getTasks/{projectId}")
    public Map getTasks(@PathVariable int projectId){
        return Map.of("tasks",taskService.getTasks(projectId));
    }

    //添加任务
    @PostMapping("addTasks")
    public Map addTasks(@RequestBody Project project) {
        Project oldProject = projectService.findProjectById(project.getId());
        if (oldProject == null) {
            return Map.of("message", new MessageVO("添加失败，项目不存在"));
        } else {
            projectService.addTasks(project);
            return Map.of("message", new MessageVO("添加成功"), "tasks", taskService.getTasks(project.getId()));
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
        System.out.println(task.getId());
        System.out.println(task.getName());
        System.out.println(task.getStartTime());
        taskService.updateTask(task);
        return Map.of("task",taskService.getTaskById(task.getId()));
    }

    //查看经理管理的所有员工
    @GetMapping("getEmployee")
    public Map getEmployee() {
        List<Project> projects = projectService.getProjects(requestComponent.getUid());
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
    @GetMapping("getAvailable")
    public Map getAvailable() {
        List<Project> projects = projectService.getProjects(requestComponent.getUid());
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
        return Map.of("message",new MessageVO("添加成功"));
    }

    //为任务添加员工
    @PostMapping("addWorker")
    public Map addWorker(@RequestBody List<TaskEmployee> taskEmployees) {
        if (taskEmployees == null || taskEmployees.size() == 0){
            return Map.of("message",new MessageVO("未传数据"));
        }
        taskEmployeeService.addWorker(taskEmployees);
        return Map.of("taskEmployee",taskEmployeeService.getEmployeeList(taskEmployees.get(0).getTask().getId()));
    }
    //为任务删除员工
    @PostMapping("deleteWorker")
    public Map deleteWorker(@RequestBody List<TaskEmployee> taskEmployees){
        if (taskEmployees == null || taskEmployees.size() == 0) {
            return Map.of("message", new MessageVO("任务不存在"));
        }else {
            List<Integer> taskEmpIds = taskEmployees.stream().map(TaskEmployee::getId).collect(Collectors.toList());
            taskEmployeeService.deleteWorker(taskEmpIds);
            return Map.of("taskEmployee",taskEmployeeService.getEmployeeList(taskEmployees.get(0).getTask().getId()));
        }
    }
}
