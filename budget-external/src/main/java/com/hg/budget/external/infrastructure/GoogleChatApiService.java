package com.hg.budget.external.infrastructure;

import com.hg.budget.external.infrastructure.request.SendRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "google-chat-api-service")
public interface GoogleChatApiService {

    @PostMapping
    String send(@RequestBody SendRequest request);
}
