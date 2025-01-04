package com.sight.start.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProduceService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProduceService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendMessage(String msg) {
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send("sight-topic", msg);

        send.whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Unable to send message: " + ex.getMessage());
            } else {
                System.out.println("Message sent successfully");
            }
        });

        return "Message sent successfully";
    }
}
