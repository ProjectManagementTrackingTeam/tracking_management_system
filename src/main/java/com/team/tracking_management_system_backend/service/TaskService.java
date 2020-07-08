package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Project;
import com.team.tracking_management_system_backend.entity.Task;
import com.team.tracking_management_system_backend.repository.TaskRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Data
@Transactional
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasks(int projectId) {
        return taskRepository.getTasks(projectId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTasks(List<Integer> taskIdList) {
        taskRepository.deleteTasks(taskIdList);
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        return taskRepository.getOne(id);
    }

    public void addWorker(Task task) {
        taskRepository.save(task);
    }
}
