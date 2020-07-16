package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Project;
import com.team.tracking_management_system_backend.entity.Task;
import com.team.tracking_management_system_backend.repository.ProjectRepository;
import com.team.tracking_management_system_backend.repository.TaskRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Data
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    public void addProject(Project project) {
        projectRepository.save(project);
    }

    public Project findProjectByName(int managerId, String name) {
        Optional<Project> optional = projectRepository.findProjectByName(managerId, name);
        if (optional.isPresent()) {
            List<Task> tasks = taskRepository.getTasks(optional.get().getId());
            //设置进度
            setProcess(optional.get(), tasks);
        }
        return optional.orElse(null);
    }

    public List<Project> getProjects(int managerId) {
        List<Task> allTasks = taskRepository.findAll();
        List<Project> projects = projectRepository.getProjects(managerId);
        //为每个project都加上了进度
        projects.stream().map(p -> p.getName()).forEach(System.out::print);
        allTasks.stream().map(t -> t.getProject().getId()).forEach(System.out::print);
        List<Project> collect = projects.stream().map((e) -> {
            List<Task> tasks = allTasks.stream().
                    filter((t) -> t.getProject().getId() == e.getId()).collect(Collectors.toList());
            setProcess(e, tasks);
            return e;
        }).collect(Collectors.toList());
        return collect;
    }

    public Project findProjectById(int projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            List<Task> tasks = taskRepository.getTasks(project.get().getId());
            setProcess(project.get(), tasks);
        }
        return project.orElse(null);
    }

    public void deleteProjects(List<Integer> projectsIdList) {
        projectRepository.deleteProjects(projectsIdList);
    }

    public void transferProject(List<Integer> transferIds, int managerId) {
        projectRepository.transferProjects(transferIds, managerId);
    }

    public void addTask(Task task) {
//        taskRepository.deleteTasksFromProject(project.getId());
//        taskRepository.saveAll(project.getTasks().stream()
//                .map((t)->{t.setProject(new Project(project.getId())); return t;})
//                .collect(Collectors.toList()));

        taskRepository.save(task);

    }

    public boolean updateProject(Project project) {
        Project projectById = projectRepository.getOne(project.getId());
        if (projectById == null) {
            return false;
        } else {
            projectById.setEndTime(project.getEndTime());
            projectById.setStartTime(project.getStartTime());
            projectById.setName(project.getName());
            projectById.setEmergencyDegree(project.getEmergencyDegree());
            return true;
        }

    }

    private void setProcess(Project project, List<Task> tasks) {
        if (tasks == null || tasks.size() == 0) {
            project.setProcess(0.0);
            return;
        }
        int complete = tasks.stream()
                .filter((t) -> t.getIsFinished() == 1)
                .mapToInt(Task::getWeight).sum();
        int sum = tasks.stream().mapToInt(Task::getWeight).sum();
        project.setProcess(complete * 1.00 / sum * 100.00) ;
    }
}
