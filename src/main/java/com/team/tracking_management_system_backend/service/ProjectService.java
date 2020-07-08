package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Project;
import com.team.tracking_management_system_backend.entity.Task;
import com.team.tracking_management_system_backend.repository.ProjectRepository;
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
    private TaskService taskService;
    public void addProject(Project project){
        projectRepository.save(project);
    }
    public Project findProjectByName(int managerId,String name){
        Optional<Project> optional = projectRepository.findProjectByName(managerId,name);
        if (optional.isPresent()){
            List<Task> tasks = taskService.getTasks(optional.get().getId());
            //设置进度
            setProcess(optional.get(),tasks);
        }
        return optional.orElse(null);
    }
    public List<Project> getProjects(int managerId){
        List<Task> allTasks = taskService.getAllTasks();
        List<Project> projects = projectRepository.getProjects(managerId);
        //为每个project都加上了进度
        projects.stream().map((e)->{
            List<Task> tasks = allTasks.stream().
                    filter((t) -> t.getProject().getId() == e.getId()).collect(Collectors.toList());
            setProcess(e,tasks);
            return e;
            });
        return projects;
    }
    public Project findProjectById(int projectId){
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()){
            List<Task> tasks = taskService.getTasks(project.get().getId());
            setProcess(project.get(),tasks);
        }
        return project.orElse(null);
    }
    public void deleteProjects(List<Integer> projectsIdList){
        projectRepository.deleteProjects(projectsIdList);
    }
    public void transferProject(List<Integer> transferIds,int managerId){
        projectRepository.transferProjects(transferIds, managerId);
    }
    public void addTasks(List<Task> addTasks,Project oldProject){
        List<Task> tasks = taskService.getTasks(oldProject.getId());
        tasks.addAll(addTasks);
        oldProject.setTasks(tasks);
        projectRepository.save(oldProject);
    }
    private void setProcess(Project project, List<Task> tasks){
        if(tasks == null || tasks.size() == 0){
            project.setProcess(0.0);
            return;
        }
        project.setProcess((tasks.stream()
                .filter((t)->t.getIsFinished() == 1)
                .collect(Collectors.toList())
                .size())/tasks.size());
    }
}
