package com.hg.budget.external.client;

public interface NotificationSender {

    void send(String receiver, String title, String content);
}
