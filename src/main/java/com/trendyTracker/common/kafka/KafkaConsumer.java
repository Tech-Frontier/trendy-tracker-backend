package com.trendyTracker.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    // @KafkaListener(topics = "logger", groupId = "my-group")
    // public void listenToTopic1(ConsumerRecord<String, String> record) {
    //     String key = record.key();
    //     String value = record.value();
    //     System.out.println("Received message - Key: " + key + ", Value: " + value);
    // }

    // @KafkaListener(topics = "error", groupId = "my-group")
    // public void listenToTopic2(ConsumerRecord<String, String> record) {
    //     String key = record.key();
    //     String value = record.value();
    //     System.out.println("Received message - Key: " + key + ", Value: " + value);
    // }
}
