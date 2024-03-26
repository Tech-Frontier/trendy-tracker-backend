package com.trendytracker.adaptors.message;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendytracker.domain.job.recruit.Recruit;
import com.trendytracker.domain.job.recruit.RecruitConsumer;
import com.trendytracker.domain.job.recruit.RecruitService;

@Service
public class RecruitConsumerImpl implements RecruitConsumer<ConsumerRecord<String, String>>{    
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private KafkaProducer kafkaProducer;
    
    private Logger logger = LoggerFactory.getLogger(RecruitConsumerImpl.class);

    @Override
    @KafkaListener(topics = "RegistJob" ,groupId = "job-regisit")
    public void registJob(ConsumerRecord<String, String> message) throws JsonMappingException, JsonProcessingException {
    /*
     * 채용공고 등록 event
     */
        JsonNode jsonNode = new ObjectMapper().readTree(message.value());
        String url = jsonNode.get("url").asText();
        String company = jsonNode.get("company").asText();
        String jobCategory = jsonNode.get("jobCategory").asText();        
        String uuid = extractUUID(message);

        try{
            var recruit = recruitService.registRecruit(url, company, jobCategory);
            logger.info("Consumed RegistJob Topic: id:" + recruit.getId() + " header: " + uuid);
        }
        catch(Exception ex){
            Map<String, String> params = new HashMap<>();
            params.put("url", url);
            params.put("company", company);
            params.put("jobCategory", jobCategory);
            params.put("uuid", uuid);
            params.put("error", ex.getMessage());
            kafkaProducer.sendMessage("error", params, uuid);

            logger.error(uuid, ex.getMessage());
        }
    }

    @Override
    @KafkaListener(topics = "UpdateJob", groupId = "job-update")
    public void updateJob(ConsumerRecord<String, String> message)
            throws JsonMappingException, JsonProcessingException {
    /*
     * 채용 공고 수정 event
     */
        JsonNode jsonNode = new ObjectMapper().readTree(message.value());
        String url = jsonNode.get("url").asText();
        String uuid = extractUUID(message);
        
        try{
            Recruit recruit = recruitService.updateRecruitInfo(url);
            logger.info("Consumed UpdateJob Topic: id:" + recruit.getId() + " header: " + uuid);
        }
        catch (Exception ex){
            Map<String,String> params = new HashMap<>();
            params.put("url", url);
            params.put("uuid", uuid);
            params.put("error", ex.getMessage());
            kafkaProducer.sendMessage("error",params, uuid);

            logger.error(uuid, ex.getMessage());
        }
    }

    @Override
    public void deleteJob(ConsumerRecord<String, String> message) {
    /*
     * 채용공고 등록 event
     */
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteJobUrl'");
    }

    private String extractUUID(ConsumerRecord<String, String> message) {
        Header[] headers = message.headers().toArray();
        String uuid ="";

        for(Header header : headers){
            if("uuid".equals(header.key()))
                uuid = new String(header.value(), StandardCharsets.UTF_8);
        }
        return uuid;
    }
}
