package com.mustafafindik.producerservice.controller;

import model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/message")
public class UserController {

    @Value("${mustafafindik.kafka.topic}")
    private String topic;
    private final KafkaTemplate<String, User> kafkaTemplate;

    public UserController(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @PostMapping
    public void sendMessage(@RequestBody User user){
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), user);
    }
}
