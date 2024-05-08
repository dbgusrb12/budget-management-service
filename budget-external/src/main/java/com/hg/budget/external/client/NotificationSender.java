package com.hg.budget.external.client;

import com.hg.budget.external.client.dto.Notification;

public interface NotificationSender {

    void send(String receiver, String title, String content);

    void send(Notification notification);
}
