package com.trendyTracker.common.kafka;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, String value, String header) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, key, value);

        RecordHeader recordHeader = new RecordHeader("uuid", header.getBytes(StandardCharsets.UTF_8));
        record.headers().add(recordHeader);

        kafkaTemplate.send(record);
    }
}
