package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.MyProjectFile;
import com.team.tracking_management_system_backend.repository.ProjectFileRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@Data
@Transactional
public class ProjectFileService {
    @Autowired
    ProjectFileRepository projectFileRepository;

    public List<MyProjectFile> getFileFromProject(int projectId){
       return  projectFileRepository.getFileByProject(projectId);
    }

    public void addProjectFile(MyProjectFile projectFile){
        projectFileRepository.save(projectFile);
    }
    public List<MyProjectFile> getProjectFiles(){
        return projectFileRepository.findAll();
    }
    public void deleteProjectFile(String fileKey){
        projectFileRepository.deleteProjectFile(fileKey);
    }
}
