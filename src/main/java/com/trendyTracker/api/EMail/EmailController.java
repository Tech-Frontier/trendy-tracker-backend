package com.trendyTracker.api.EMail;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.Appservice.service.EmailService;
import com.trendyTracker.common.config.Loggable;
import com.trendyTracker.common.response.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;



@Loggable
@Tag(name = "Email", description = "이메일 인증")
@RequestMapping("/api/email")
@RestController
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "이메일 인증 번호 발송")
    @PostMapping(value = "/signup/send")
    public Response<Void> sendVerificationEmail(@RequestBody @Validated emailRequest emailRequest,
    HttpServletRequest request, HttpServletResponse response) throws MessagingException{
        emailService.sendVerificationEmail(emailRequest.email);

        addHeader(request, response);
        return Response.success(200, "이메일 인증 번호가 전송 되었습니다.");
    }

    private void addHeader(HttpServletRequest request, HttpServletResponse response) {
        UUID uuid = (UUID) request.getAttribute("uuid");
        response.addHeader("uuid", uuid.toString());
    }


    @Data
    static class emailRequest {
        @NotNull
        @Email
        @Schema(description = "email", example = "wlstncjs1234@naver.com", type = "String")
        private String email;
    }
}
