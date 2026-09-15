package org.example.jupjupscheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jupjupscheduler.client.JupjupApiClient;
import org.example.jupjupscheduler.client.TicketServerClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerJob implements CommandLineRunner {

    private final JupjupApiClient jupjupApiClient;
    private final TicketServerClient ticketServerClient;
    private final ConfigurableApplicationContext context;

    @Override
    public void run(String... args) {
        String jobType = System.getenv("JOB_TYPE");

        if (jobType == null || jobType.isBlank()) {
            log.error("JOB_TYPE 환경변수가 없습니다.");
            shutdown(1);
            return;
        }

        log.info("스케줄러 작업 시작 - JOB_TYPE={}", jobType);

        try {
            switch (jobType) {
                case "PROGRAM_SYNC" -> {
                    log.info("프로그램 동기화 시작");
                    jupjupApiClient.syncPrograms();
                    log.info("프로그램 동기화 성공");
                }

                case "PERFORMANCE_SEAT_SYNC" -> {
                    log.info("회차 및 좌석 동기화 시작");
                    jupjupApiClient.syncPerformanceAndSeats();
                    log.info("회차 및 좌석 동기화 성공");
                }

                case "TICKET_POLLING" -> {
                    log.info("티켓 polling 시작");
                    jupjupApiClient.pollTickets();
                    log.info("티켓 polling 성공");
                }

                case "RESERVATION_EXPIRE" -> {
                    log.info("티켓서버 예약 만료 처리 시작");
                    ticketServerClient.expireReservations();
                    log.info("티켓서버 예약 만료 처리 성공");
                }

                case "RESERVATION_EXPIRE_JUPJUP" -> {
                    log.info("줍줍서버 예약 만료 처리 시작");
                    jupjupApiClient.expireReservations();
                    log.info("줍줍서버 예약 만료 처리 성공");
                }

                default -> {
                    log.error("알 수 없는 JOB_TYPE: {}", jobType);
                    shutdown(1);
                    return;
                }
            }

            log.info("스케줄러 작업 종료 - JOB_TYPE={}", jobType);
            shutdown(0);

        } catch (Exception e) {
            log.error("스케줄러 작업 실패 - JOB_TYPE={}", jobType, e);
            shutdown(1);
        }
    }

    private void shutdown(int exitCode) {
        int code = SpringApplication.exit(context, () -> exitCode);
        System.exit(code);
    }
}