package com.team.tracking_management_system_backend.controller;

import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.service.UserService;
import com.team.tracking_management_system_backend.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("updatePassword")
    public Map updatePassword(@RequestBody User user){
        //没有输入相关信息
        userService.updatePassword(user);
        return Map.of("message",new MessageVO("修改成功"),MessageVO.STATE,MessageVO.State.FAIL);
    }
}
