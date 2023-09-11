package com.trendyTracker.Appservice.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendVerificationEmail(String to) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
        String verificationCode = generateVerificationCode();

        String htmlMsg = " <div style='background-color: #F6F7FB; \n" +
                "                padding-top:80px;\n" +
                "                width:750px;\n" +
                "                height:400px;\n" +
                "                padding-left:60px;'> \n" +
                " <h1 style='color: #1B1D1F;\n" +
                "                    font-style: Pretendard; \n" +
                "                    font-size:40px;\n" +
                "                    line-height:22px;\n" +
                "                    padding-bottom: 19px; \n" +
                "                    font-weight: 700; '>\n" +
                " Tredny-Tracker 이메일 인증</h1>\n" +
                " <div style='color: #626870; \n" +
                "                    font-style:Pretendard;\n" +
                "                    font-size:28px; \n" +
                "                    line-height:46px;\n" +
                "                    padding-bottom: 36px; \n" +
                "                    font-weight: 500;'>\n" +
                " 아래 인증번호를 웹 화면에 입력해주세요 </div>\n" +
                " <div style='border:1px solid white;\n" +
                "                    background-color: white;\n" +
                "                    display: flex; \n" +
                "                    flex-direction: column;\n" +
                "                    justify-content: center;\n" +
                "                    align-items: center; \n" +
                "                    font-size: 14px;\n" +
                "                    height: 191px; \n" +
                "                    width: 690px;'>\n" +
                " <div style='padding:54px 0;'>\n" +
                " <div style='color: #626870;\n" +
                "                            font-style: Pretendard; \n" +
                "                            font-weight: 500;\n" +
                "                            font-size:22px;\n" +
                "                            line-height:20px;\n" +
                "                            justify-content: center;\n" +
                "                            padding-bottom:15px;\n" +
                "                '>EMAIL 인증 코드 입니다</div>\n" +
                " <div>\n" +
                " <strong style='color: #8673FF;\n" +
                "                                font-style: Pretendard;     \n" +
                "                                font-size: 48px;\n" +
                "                                font-weight: 700;\n" +
                "                                line-height:48px;\n" +
                "                                '>\n" + verificationCode +
                " </strong>\n" +
                " </div>\n" +
                " </div>\n" +
                " </div>\n" +
                " </div>\n";

        helper.setFrom("admin@trendy-tracker.kr");
        helper.setTo(to);
        helper.setSubject("Trendy-Tracker이메일 인증");
        helper.setText(htmlMsg, true);
        javaMailSender.send(message);

        return verificationCode;
    }


    private String generateVerificationCode(){
        String numbers ="";
        Random Random = new Random();
        int bound = 9; // between 1 ~ 9 number

        while(numbers.length() <6){
            String randomNumber = String.valueOf(Random.nextInt(bound));
            numbers += randomNumber;
        }
        return numbers;
    }
}
