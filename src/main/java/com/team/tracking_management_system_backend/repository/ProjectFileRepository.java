package com.team.tracking_management_system_backend.repository;

import com.team.tracking_management_system_backend.entity.MyProjectFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectFileRepository extends BaseRepository<MyProjectFile, Integer> {
    @Query("from MyProjectFile pf where pf.project.id =:projectId")
    List<MyProjectFile> getFileByProject(int projectId);

    @Modifying
    @Query("delete from MyProjectFile pf where pf.fileKey =:fileKey")
    void deleteProjectFile(String fileKey);
}
