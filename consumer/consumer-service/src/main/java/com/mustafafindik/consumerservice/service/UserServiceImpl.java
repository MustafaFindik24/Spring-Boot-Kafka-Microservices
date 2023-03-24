package com.mustafafindik.consumerservice.service;

import com.mustafafindik.consumerservice.repository.UserRepository;
import model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void saveUser(User user) {
        User saveUser = new User();
        saveUser.setUsername(user.getUsername());
        saveUser.setPassword(user.getPassword());
        userRepository.save(saveUser);
        log.info("User saved to the database : " + saveUser.toString());
    }
}
