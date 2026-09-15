package org.example.jupjupscheduler.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class JupjupApiClient {

    private final RestClient restClient;

    public JupjupApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${jupjup.api.url}") String url
    ) {
        this.restClient = restClientBuilder
                .baseUrl(url)
                .build();
    }

    public void syncPrograms() {
        restClient.post()
                .uri("/api/internal/scheduler/program-sync")
                .retrieve()
                .toBodilessEntity();
    }

    public void syncPerformanceAndSeats() {
        restClient.post()
                .uri("/api/internal/scheduler/performance-seat-sync")
                .retrieve()
                .toBodilessEntity();
    }

    public void pollTickets() {
        restClient.post()
                .uri("/api/internal/scheduler/ticket-polling")
                .retrieve()
                .toBodilessEntity();
    }

    public void expireReservations() {
        restClient.post()
                .uri("/api/internal/scheduler/reservations/expire")
                .retrieve()
                .toBodilessEntity();
    }
}