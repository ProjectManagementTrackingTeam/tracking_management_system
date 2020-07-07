package com.team.tracking_management_system_backend.controller;


import com.team.tracking_management_system_backend.component.EncryptComponent;
import com.team.tracking_management_system_backend.component.MyToken;
import com.team.tracking_management_system_backend.component.RequestComponent;
import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/")
public class LoginController {
    @Value("${my.admin}")//@Value("${my.student}")
    private String roleAdmin;
    @Value("${my.manager}")
    private String roleManager;
    @Value("${my.employee}")
    private String roleEmplyee;

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private EncryptComponent encrypt;
    @Autowired
    private RequestComponent requestComponent;


    @PostMapping("login")
    public Map login(@RequestBody User loginUser, HttpServletResponse response){
        log.debug("{}", "login");
        User user = Optional.ofNullable(userService.getUserByNumber(loginUser.getNumber()))
                .filter(u->encoder.matches(loginUser.getPassword(), u.getPassword()))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"用户名或密码错误"));
        MyToken token  = new MyToken(user.getId(), user.getRole());
        String auth = encrypt.encryptToken(token);
        response.setHeader(MyToken.AUTHORIZATION, auth);//以键值对形式放入头中
        log.debug("{}", "登陆成功");
//        String roleCode = null;
//        if(user.getRole()== User.Role.ADMIN)
//            roleCode = roleAdmin;
//        if(user.getRole()== User.Role.MANAGER)
//            roleCode = roleManager;
//        if(user.getRole()== User.Role.EMPLOYEE)
//            roleCode = roleEmplyee;

        String roleCode = user.getRole()== User.Role.ADMIN? roleAdmin:roleManager;

        return Map.of("role",roleCode);//告诉前端你是什么身份，前端渲染不同界面
    }


}
