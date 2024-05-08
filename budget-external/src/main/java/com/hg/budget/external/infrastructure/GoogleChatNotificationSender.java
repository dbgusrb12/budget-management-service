package com.hg.budget.external.infrastructure;

import com.hg.budget.external.client.NotificationSender;
import lombok.RequiredArgsConstructor;
import com.hg.budget.external.infrastructure.request.SendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleChatNotificationSender implements NotificationSender {

    private final GoogleChatApiService googleChatApiService;

    @Override
    public void send(String receiver, String title, String content) {
        log.info("receiver: {}, title: {}, content: {}", receiver, title, content);
        final String text = title + "\n\n" + content;
        final SendRequest request = new SendRequest(text);
        String response = googleChatApiService.send(request);
        log.info("response : {}", response);
    }
}
