package com.ch.notificationservice.consumers;

import com.ch.core.chcore.events.HighMsg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for high priority messages.
 * This class listens to the "highTopic" Kafka topic and processes incoming HighMsg messages.
 * It is part of the notification service.
 *
 * @author caito
 *
 */
@Component
public class HiighMsgConsumer {

    /**
     * Listens to the "highTopic" Kafka topic and processes HighMsg messages.
     *
     * @param msg the HighMsg message received from the Kafka topic
     */
    @KafkaListener(topics = "highTopic", groupId = "high-user-group")
    public void handleUserRegistry(HighMsg msg){
        System.out.println("Received message: " + msg.getEmail() + " " + msg.getUsername() + " " + msg.getValidationToken());
    }

    @KafkaListener(topics = "enableUserTopic", groupId = "high-enable-group")
    public void handleEnableUser(String msg){
        System.out.println("Received message: " + msg);
    }
}
