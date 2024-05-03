package com.hg.budget.client.notification.infrastructure;

import com.hg.budget.client.notification.client.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoNotificationSender implements NotificationSender {


    @Override
    public void send(String receiver, String title, String content) {
        log.info("receiver: {}, title: {}, content: {}", receiver, title, content);
    }
}
