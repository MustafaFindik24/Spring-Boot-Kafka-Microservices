package com.mustafafindik.producerservice.producer;

import model.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, User> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void userProducer(User user){
        kafkaTemplate.send("${mustafafindik.kafka.topic}", UUID.randomUUID().toString(),user);
    }
}
