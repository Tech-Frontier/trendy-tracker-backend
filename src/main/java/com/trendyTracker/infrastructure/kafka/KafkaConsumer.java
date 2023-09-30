package com.trendyTracker.infrastructure.kafka;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyTracker.Job.service.RecruitService;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;

@Service
public class KafkaConsumer {
    @Autowired
    private RecruitService recruitService;
    static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "RegistJob", groupId = "my-group")
    public void listenToRegistJob(ConsumerRecord<String, String> record, @Header(name = "uuid") String uuid) throws NoResultException, IOException {
    /*
     * 채용공고 등록
     */
        String value = record.value();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(value);
        String url = jsonNode.get("url").asText();
        String company = jsonNode.get("company").asText();
        String occupation = jsonNode.get("occupation").asText();


        long id = recruitService.regisitJobPostion(url, company, occupation);
        logger.info("Consumed RegistJob Topic: id:" + id + " header: " + uuid);
    }
}
