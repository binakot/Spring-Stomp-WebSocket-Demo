package com.example.demo.service;

import com.example.demo.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler;
import org.springframework.messaging.simp.broker.DefaultSubscriptionRegistry;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.DESTINATION_HEADER;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER;

@Service
@EnableScheduling
public class StatusService {

    private final Set<Integer> subscriptionIds = ConcurrentHashMap.newKeySet();

    private final SimpMessagingTemplate template;
    private final DefaultSubscriptionRegistry registry;

    @Autowired
    public StatusService(final SimpMessagingTemplate template,
                         @Qualifier("simpleBrokerMessageHandler") final AbstractBrokerMessageHandler handler) {
        this.template = template;
        this.registry = (DefaultSubscriptionRegistry) ((SimpleBrokerMessageHandler) handler).getSubscriptionRegistry();
    }

    public void registerSubscriptionById(final Integer id) {
        subscriptionIds.add(id);
    }

    @Scheduled(initialDelay = 10_000L, fixedDelay = 10_000L)
    public void publish() {
        subscriptionIds.forEach(id -> {
            final String destination = "/topic/status/" + id;

            final MultiValueMap<String, String> subscriptions = registry.findSubscriptions(new DummyDestinationMessage(destination));
            if (subscriptions.isEmpty()) {
                System.out.printf("NO SUBSCRIPTION WITH ID: %d. IT WILL BE REMOVED!\r\n", id);

                subscriptionIds.remove(id);
            } else {
                System.out.printf("SEND SUBSCRIPTION WITH ID: %d FOR %d SUBSCRIBERS\r\n", id, subscriptions.size());

                template.convertAndSend(destination, Status.of(id));
            }
        });
    }

    private static final class DummyDestinationMessage extends GenericMessage<Status> {

        private static final Status dummyStatus = Status.of(0);

        public DummyDestinationMessage(final String destination) {
            super(dummyStatus, new MessageHeaders(Map.of(
                MESSAGE_TYPE_HEADER, SimpMessageType.MESSAGE,
                DESTINATION_HEADER, destination
            )));
        }
    }
}
