package com.wayz.service;

import com.wayz.dto.Notification;

public interface MailService {
    void sendMailNotification(Notification notification);
}
