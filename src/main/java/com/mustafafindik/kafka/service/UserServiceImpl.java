package com.mustafafindik.kafka.service;

import model.entity.User;
import com.mustafafindik.kafka.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final KafkaProducer kafkaProducer;

    public UserServiceImpl(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
    @Override
    public void createUser(User user) {
        //User user = modelMapper.map(userDto, User.class);
        User saveUser = new User();
        saveUser.setUsername(saveUser.getUsername());
        saveUser.setPassword(saveUser.getPassword());
        log.info("User class send to the queue : " + saveUser.toString());
        kafkaProducer.userProducer(saveUser);

    }
}
