package com.sooscode.sooscode_api.application.livekit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveKitWebhookController {

    @PostMapping(value = "/livekit-webhook", consumes = {"application/webhook+json", "application/json"})
    public ResponseEntity<String> receiveWebhook(
            @RequestBody String rawJson,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {

        System.out.println("=== LiveKit Webhook Received ===");
        System.out.println("Authorization: " + authorization);
        System.out.println("Body: " + rawJson);

        return ResponseEntity.ok("OK");
    }
}
