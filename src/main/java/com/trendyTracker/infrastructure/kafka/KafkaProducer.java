package com.trendyTracker.infrastructure.kafka;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, String value, String header) {
    /*
     * Logging 
     */
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, key, value);

        RecordHeader recordHeader = new RecordHeader("uuid", header.getBytes(StandardCharsets.UTF_8));
        record.headers().add(recordHeader);

        kafkaTemplate.send(record);
    }

    public void sendMessage(String topic, Map<String,String> messageMap, String header) throws JsonProcessingException{
    /*
     * Recruit Scrapping
     */
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(messageMap);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);

        RecordHeader recordHeader = new RecordHeader("uuid", header.getBytes(StandardCharsets.UTF_8));
        record.headers().add(recordHeader);
        
        kafkaTemplate.send(record);
    }
}
