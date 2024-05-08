package com.hg.budget.external.infrastructure;

import com.hg.budget.external.client.NotificationSender;
import com.hg.budget.external.client.dto.Notification;
import com.hg.budget.external.infrastructure.request.Card;
import com.hg.budget.external.infrastructure.request.SendRequest;
import lombok.RequiredArgsConstructor;
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
        final SendRequest request = SendRequest.ofText(text);
        String response = googleChatApiService.send(request);
        log.info("response : {}", response);
    }

    @Override
    public void send(Notification notification) {
        log.info("notification: {}", notification);
        Card card = Card.of(notification.getTitle(), notification.getSubtitle());
        notification.getContents()
            .forEach(content -> card.addSection(content.getHeader(), content.getContent()));
        final SendRequest request = SendRequest.ofCard(card);
        String response = googleChatApiService.send(request);
        log.info("response : {}", response);
    }
}
