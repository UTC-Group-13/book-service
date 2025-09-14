package org.example.bookservice.service;

import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.entity.Email;

import java.util.List;

public interface EmailService {
    void sendEmail(SendEmailRequest sendEmailRequest);

    List<Email> getAllEmailWithStatus(Integer status);
}