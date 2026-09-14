package org.example.jupjupscheduler.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jupjupscheduler.client.JupjupApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramSyncJob implements CommandLineRunner {

    private final JupjupApiClient jupjupApiClient;
    private final ConfigurableApplicationContext context;

    @Override
    public void run(String... args) {
        log.info("프로그램 동기화 시작");

        try {
            jupjupApiClient.syncPrograms();
            log.info("프로그램 동기화 성공");

            SpringApplication.exit(context, () -> 0);
        } catch (Exception e) {
            log.error("프로그램 동기화 실패", e);

            SpringApplication.exit(context, () -> 1);
        }
    }
}