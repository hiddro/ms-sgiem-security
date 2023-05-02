package com.sgiem.ms.security.config.kafka;

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
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public void sendMessage(String key, String value) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, value);
        future.whenComplete(new BiConsumer<SendResult<String, String>, Throwable>() {
            @Override
            public void accept(SendResult<String, String> stringStringSendResult, Throwable throwable) {
                logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
                MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
                listenerContainer.start();
            }
        });
    }

}
