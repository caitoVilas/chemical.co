package com.ch.notificationservice.consumers;

import com.ch.core.chcore.events.HighMsg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HiighMsgConsumer {

    @KafkaListener(topics = "highTopic", groupId = "high-user-group")
    public void handleUserRegistry(HighMsg msg){
        System.out.println("Received message: " + msg.getEmail() + " " + msg.getUsername());
    }
}
