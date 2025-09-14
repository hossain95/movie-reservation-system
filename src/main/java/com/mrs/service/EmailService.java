package com.mrs.service;


import com.mrs.dto.EmailInfo;

public interface EmailService {
    void sendOtpVerificationMail(EmailInfo emailInfo);

    void sendPasswordCreateMail(EmailInfo emailInfo);

    void sendPasswordForgotMail(EmailInfo emailInfo);

    void sendMessage(EmailInfo emailInfo);
}
