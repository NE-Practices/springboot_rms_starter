package com.spring.rms.services;


import com.spring.rms.models.Resource;

public interface IMailService {
    void sendResourceCreatedEmail(String username, Resource resource);
    void sendResourceUpdatedEmail(String username, Resource resource);
    void sendResourceDeletedEmail(String username, Resource resource);
    void sendBookingStatusEmail(String username, Resource resource, String action, String status);
    void sendBookingStatusEmailToDefault(Resource resource, String action, String status);
}