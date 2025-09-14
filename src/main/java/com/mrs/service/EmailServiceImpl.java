package com.mrs.service;

import com.mrs.dto.EmailInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String from;

    @Value("${email.title}")
    private String otpTitle;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpVerificationMail(EmailInfo emailInfo) {
        log.info("Send otp verification email to {}", emailInfo.getEmail());
        sendMail(emailInfo);
    }

    @Override
    public void sendPasswordForgotMail(EmailInfo emailInfo) {
        sendMail(emailInfo);
    }

    @Override
    public void sendMessage(EmailInfo emailInfo) {
        sendMail(emailInfo);
    }

    @Override
    public void sendPasswordCreateMail(EmailInfo emailInfo) {
        log.info("Send session id mail to {}", emailInfo.getEmail());
        sendMail(emailInfo);
    }

    private void sendMail(EmailInfo emailInfo) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setTo(emailInfo.getEmail());
            message.setSubject(emailInfo.getSubject());
            message.setFrom(from, otpTitle);
            message.setText(emailInfo.getBody(), true);
            message.setSentDate(new Date());
            mailSender.send(mimeMessage);
            log.info("Email sent successfully");
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.warn("Failed to send email");
            throw new RuntimeException(e);
        }
    }
}
