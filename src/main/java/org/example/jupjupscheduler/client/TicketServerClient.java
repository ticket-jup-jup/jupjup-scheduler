package org.example.jupjupscheduler.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TicketServerClient {

    private final RestClient restClient;
    private final String url;

    public TicketServerClient(
            RestClient.Builder restClientBuilder,
            @Value("${ticket.server.url}") String url
    ) {
        this.url = url;
        this.restClient = restClientBuilder
                .baseUrl(url)
                .build();
    }

    public void expireReservations() {
        restClient.post()
                .uri("/api/internal/scheduler/reservations/expire")
                .retrieve()
                .toBodilessEntity();
    }
}