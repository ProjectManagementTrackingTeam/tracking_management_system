package com.team.tracking_management_system_backend.service;


import com.team.tracking_management_system_backend.entity.Admin;
import com.team.tracking_management_system_backend.entity.Manager;
import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.repository.AdminRepositpry;
import com.team.tracking_management_system_backend.repository.ManagerRepository;
import com.team.tracking_management_system_backend.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@Data
@Transactional
public class AdminService  {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AdminRepositpry adminRepositpry;
    @Autowired
    private ManagerRepository managerRepository;

    public void updatePassword(Admin admin){
        User user = admin.getUser();
        User oldUser = adminRepositpry.getOne(admin.getId()).getUser();
        //要将加密之后的放入
        oldUser.setPassword(encoder.encode(user.getPassword()));
        admin.setUser(oldUser);
        adminRepositpry.save(admin);
    }
    public void addAdmin(Admin admin){
        adminRepositpry.save(admin);
    }
    public void addManager(Manager manager){
//        if(managerRepository.findByNumber(manager.getUser().getNumber())==null){
//            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
//        }

        User u = manager.getUser();
        u.setPassword(encoder.encode(String.valueOf(u.getNumber())));
        u.setRole(User.Role.MANAGER);
        log.debug("{}",manager);
        managerRepository.save(manager);
    }
    public List<Manager> getManagers(){
        return managerRepository.findAll();
    }
    public void deleteManager(int mid){
        managerRepository.deleteById(mid);
    }
}
