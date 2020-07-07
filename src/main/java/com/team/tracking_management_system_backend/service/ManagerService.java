package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.Manager;
import com.team.tracking_management_system_backend.repository.ManagerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@Transactional
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;
}
