package com.trendyTracker.Job.repository;

import java.util.HashMap;
import java.util.Map;
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
import com.trendyTracker.infrastructure.kafka.KafkaProducer;


@Service
public class KafkaConsumerImpl implements MessageConsumer<ConsumerRecord<String, String>> {
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private KafkaProducer kafkaProducer;

    static Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    @Override
    @KafkaListener(topics = "RegistJob", groupId = "job-regist", concurrency = "3")
    public void registJobUrl(ConsumerRecord<String, String> mesage, @Header(name = "uuid") Optional<String> header) throws Exception {
        /*
         * 채용공고 등록 event
         */
        JsonNode jsonNode = new ObjectMapper().readTree(mesage.value());
        String url = jsonNode.get("url").asText();
        String company = jsonNode.get("company").asText();
        String jobCategory = jsonNode.get("jobCategory").asText();
        String uuid = header.get();
        
        try{
            long id = recruitService.regisitJobPostion(url, company, jobCategory);
            logger.info("Consumed RegistJob Topic: id:" + id + " header: " + uuid);
        }
        catch (Exception ex){
            Map<String,String> params = new HashMap<>();
            params.put("url", url);
            params.put("company", company);
            params.put("jobCategory", jobCategory);
            params.put("uuid", uuid);
            kafkaProducer.sendMessage("error", params, uuid);

            logger.error(header.get(), ex.getMessage());
        }
    }

    @Override
    @KafkaListener(topics = "UpdateJob", groupId = "job-update")
    public void updateJobUrl(ConsumerRecord<String, String> mesage, @Header(name = "uuid") Optional<String> header) throws Exception {
        /*
         * 채용 공고 수정 event
         */
        JsonNode jsonNode = new ObjectMapper().readTree(mesage.value());
        String url = jsonNode.get("url").asText();
        String uuid = header.get();
        
        try{
            long id = recruitService.updateJobPosition(url);
            logger.info("Consumed UpdateJob Topic: id:" + id + " header: " + uuid);
        }
        catch (Exception ex){
            Map<String,String> params = new HashMap<>();
            params.put("url", url);
            params.put("uuid", uuid);
            kafkaProducer.sendMessage("error",params, uuid);

            logger.error(header.get(), ex.getMessage());
        }
    }

    @Override
    public void deleteJobUrl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteJobUrl'");
    }
}
