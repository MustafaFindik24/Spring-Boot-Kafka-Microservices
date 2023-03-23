/*package com.mustafafindik.kafka.service;

import com.mustafafindik.kafka.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class MessageService {

    @KafkaListener(
        topics = "${mustafafindik.kafka.topic}",
        groupId = "${mustafafindik.kafka.group.id}"
            )
    public void listen(@Payload User user){
        log.info("Mesaj alındı. MessageID : {}, Message : {}, Date : {}",
                user.getId(),
                user.getUsername(),
                user.getPassword());
    }
// consumer service sınıfına alınacak çünkü gelen veriyi dinleyen bir servis
}*/