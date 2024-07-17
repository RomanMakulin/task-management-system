package com.wayz.view;

import com.wayz.dto.Notification;

public interface EmailMessage {
    String create(Notification notification);
}
