package com.mustafafindik.kafka.config;

import model.entity.User;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${mustafafindik.kafka.address}")
    private String kafkaAddress;
    @Value("${mustafafindik.kafka.group.id}")
    private String groupId;
    @Bean
    public KafkaTemplate<String, User> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public ProducerFactory producerFactory(){
        Map<String,Object> producer = new HashMap<>();
        producer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        producer.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,true);
        producer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        return new DefaultKafkaProducerFactory(producer);
    }

    //TODO aşağıdaki iki bean consumer service e taşınacak çünkü gelen veriyi dinleyip tüketmek için

}
