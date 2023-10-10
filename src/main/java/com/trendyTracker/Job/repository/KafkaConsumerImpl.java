package com.trendyTracker.Job.repository;

import java.util.Optional;

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

@Service
public class KafkaConsumerImpl implements MessageConsumer<ConsumerRecord<String, String>> {
    @Autowired
    private RecruitService recruitService;
    static Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    @Override
    @KafkaListener(topics = "RegistJob", groupId = "job-regist", concurrency = "3")
    public void registJobUrl(ConsumerRecord<String, String> mesage, @Header(name = "uuid") Optional<String> header) {
        /*
         * 채용공고 등록 event
         */
        try{
            String value = mesage.value();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(value);
            String url = jsonNode.get("url").asText();
            String company = jsonNode.get("company").asText();
            String occupation = jsonNode.get("occupation").asText();


            long id = recruitService.regisitJobPostion(url, company, occupation);
            logger.info("Consumed RegistJob Topic: id:" + id + " header: " + header.get());
        
        }catch (Exception ex){
            logger.error(header.get(), ex.getMessage());
        }
    }

    @Override
    @KafkaListener(topics = "UpdateJob", groupId = "job-update", concurrency = "1")
    public void updateJobUrl(ConsumerRecord<String, String> mesage, @Header(name = "uuid") Optional<String> header) {
        /*
         * 채용 공고 수정 event
         */
        try{
            String value = mesage.value();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(value);
            String url = jsonNode.get("url").asText();

            long id = recruitService.updateJobPosition(url);
            logger.info("Consumed UpdateJob Topic: id:" + id + " header: " + header.get());
        }
        catch (Exception ex){
            logger.error(header.get(), ex.getMessage());
        }
    }

    @Override
    public void deleteJobUrl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteJobUrl'");
    }

}
