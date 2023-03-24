package com.mustafafindik.consumerservice.component;

import com.mustafafindik.consumerservice.service.UserService;
import model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserConsumer {
    private final UserService userService;
    public UserConsumer(UserService userService) {
        this.userService = userService;
    }
    @KafkaListener(
            topics = "${mustafafindik.kafka.topic}",
            groupId = "${mustafafindik.kafka.group.id}")
    public void userConsumer(User user){
        log.info("User received from Kafka pool. Username : {} , password : {}",
                user.getUsername(),
                user.getPassword());
        userService.saveUser(user);
    }
}
