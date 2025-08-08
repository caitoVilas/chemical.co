package com.ch.notificationservice.consumers;

import com.ch.core.chcore.events.HighMsg;
import com.ch.notificationservice.services.contracts.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumer for high priority messages.
 * This class listens to the "highTopic" Kafka topic and processes incoming HighMsg messages.
 * It is part of the notification service.
 *
 * @author caito
 *
 */
@Component
@RequiredArgsConstructor
public class HiighMsgConsumer {
    private final MailSender mailSender;

    /**
     * Listens to the "highTopic" Kafka topic and processes HighMsg messages.
     *
     * @param msg the HighMsg message received from the Kafka topic
     */
    @KafkaListener(topics = "highTopic", groupId = "high-user-group")
    public void handleUserRegistry(HighMsg msg){
        System.out.println("Received message: " + msg.getEmail() + " " + msg.getUsername() + " "
                + msg.getValidationToken());
        Map<String, String> data = new HashMap<>();
        data.put("name", msg.getUsername());
        data.put("token", msg.getValidationToken());
        mailSender.sendEmailWithTemplate(new String[]{msg.getEmail()},
                "Account Activation - No Reply",
                "templates/account-activation.html",
                data);
    }

    /**
     * Listens to the "enableUserTopic" Kafka topic and processes incoming messages.
     *
     * @param msg the message received from the Kafka topic
     */
    @KafkaListener(topics = "enableUserTopic", groupId = "high-enable-group")
    public void handleEnableUser(String msg){
        System.out.println("Received message: " + msg);
    }
}
