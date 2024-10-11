package com.PodologiaSProject.app.services;

public interface EmailService {
    void enviarEmail(String to, String subject, String body);
}