package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    public WebSocketController(final SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/status/{id}")
    public void subscribe(final @DestinationVariable Integer id) {
        System.out.println(id);
    }

    @Scheduled(fixedDelay = 10_000L)
    public void publish() {
        template.convertAndSend("/topic/status/1337", "OLOLO");
    }
}
