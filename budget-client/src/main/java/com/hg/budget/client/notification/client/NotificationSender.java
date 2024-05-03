package com.hg.budget.client.notification.client;

public interface NotificationSender {

    void send(String receiver, String title, String content);
}
