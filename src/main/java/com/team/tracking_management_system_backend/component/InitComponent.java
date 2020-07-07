package com.team.tracking_management_system_backend.component;


import com.team.tracking_management_system_backend.entity.Admin;
import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.service.AdminService;
import com.team.tracking_management_system_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//系统初始化之后调用该组件
@Component
@Slf4j
public class InitComponent implements InitializingBean {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @Autowired
    public void afterPropertiesSet() throws Exception{
        int number = 1001;
        User user = userService.getUserByNumber(number);
        if(user == null){
            User u = new User();
            u.setName("管理员");
            u.setNumber(number);
            u.setRole(User.Role.ADMIN);
            u.setPassword(encoder.encode(String.valueOf(number)));

            Admin a = new Admin();
            a.setUser(u);
            adminService.addAdmin(a);
        }
    }
}
