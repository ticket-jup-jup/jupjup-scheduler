package org.example.jupjupscheduler.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jupjupscheduler.client.JupjupApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramSyncJob implements CommandLineRunner {

    private final JupjupApiClient jupjupApiClient;

    @Override
    public void run(String... args) {
        log.info("프로그램 동기화 시작");

        jupjupApiClient.syncPrograms();

        log.info("프로그램 동기화 종료");
    }
}