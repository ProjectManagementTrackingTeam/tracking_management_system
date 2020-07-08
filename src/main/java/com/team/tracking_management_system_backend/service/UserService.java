package com.team.tracking_management_system_backend.service;

import com.team.tracking_management_system_backend.entity.User;
import com.team.tracking_management_system_backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public User getUserByNumber(int number){
        return  userRepository.findByNumber(number);
    }
    public void updatePassword(User user){
        User oldUser = userRepository.getOne(user.getId());
        //要将加密之后的放入
        oldUser.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
