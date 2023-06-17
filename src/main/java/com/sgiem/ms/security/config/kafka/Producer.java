package com.sgiem.ms.security.config.kafka;

import com.example.UserCredentialDto;
import com.sgiem.ms.security.models.entity.UserCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "sgiem-topic";
    @Autowired
    private KafkaTemplate<String, UserCredentialDto> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public void sendMessage(String key, UserCredentialDto value) {
        CompletableFuture<SendResult<String, UserCredentialDto>> future = kafkaTemplate.send(TOPIC, key, value);
        future.whenComplete(new BiConsumer<SendResult<String, UserCredentialDto>, Throwable>() {
            @Override
            public void accept(SendResult<String, UserCredentialDto> stringStringSendResult, Throwable throwable) {
                logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
                MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
                listenerContainer.start();
            }
        });
//        CompletableFuture<SendResult<String, UserCredentialDto>> future = kafkaTemplate.send(TOPIC, key, value);
//        future.whenComplete((sendResult, throwable) -> {
//            if (throwable != null) {
//                logger.error("Failed to produce event", throwable);
//            } else {
//                logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
//
//            }
//        });
    }

}
