package com.example.demo.controller;

import com.example.demo.model.Status;
import com.example.demo.service.StatusService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final StatusService statusService;

    public WebSocketController(final StatusService statusService) {
        this.statusService = statusService;
    }

    @SubscribeMapping("/status/{id}")
    public Status subscribe(final @DestinationVariable Integer id) {
        System.out.printf("NEW SUBSCRIPTION WITH ID: %d\r\n", id);

        statusService.registerSubscriptionById(id);
        return Status.of(id);
    }
}
