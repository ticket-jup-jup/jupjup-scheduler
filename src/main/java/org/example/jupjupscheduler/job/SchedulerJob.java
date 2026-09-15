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
            log.error("scheduler_job_failure job=UNKNOWN status=FAILURE reason=JOB_TYPE_MISSING");
            shutdown(1);
            return;
        }

        long startTime = System.nanoTime();

        log.info("scheduler_job_start job={} status=START", jobType);

        try {
            executeJob(jobType);

            long durationMs = getDurationMs(startTime);

            log.info("scheduler_job_success job={} status=SUCCESS duration_ms={}", jobType, durationMs);

            shutdown(0);

        } catch (Exception e) {
            long durationMs = getDurationMs(startTime);

            log.error(
                    "scheduler_job_failure job={} status=FAILURE duration_ms={} exception={} message={}",
                    jobType,
                    durationMs,
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    e
            );

            shutdown(1);
        }
    }

    private void executeJob(String jobType) {
        switch (jobType) {
            case "PROGRAM_SYNC" -> {
                log.info("scheduler_task_start job=PROGRAM_SYNC");
                jupjupApiClient.syncPrograms();
                log.info("scheduler_task_success job=PROGRAM_SYNC");
            }

            case "PERFORMANCE_SEAT_SYNC" -> {
                log.info("scheduler_task_start job=PERFORMANCE_SEAT_SYNC");
                jupjupApiClient.syncPerformanceAndSeats();
                log.info("scheduler_task_success job=PERFORMANCE_SEAT_SYNC");
            }

            case "TICKET_POLLING" -> {
                log.info("scheduler_task_start job=TICKET_POLLING");
                jupjupApiClient.pollTickets();
                log.info("scheduler_task_success job=TICKET_POLLING");
            }

            case "RESERVATION_EXPIRE" -> {
                log.info("scheduler_task_start job=RESERVATION_EXPIRE");
                ticketServerClient.expireReservations();
                log.info("scheduler_task_success job=RESERVATION_EXPIRE");
            }

            case "RESERVATION_EXPIRE_JUPJUP" -> {
                log.info("scheduler_task_start job=RESERVATION_EXPIRE_JUPJUP");
                jupjupApiClient.expireReservations();
                log.info("scheduler_task_success job=RESERVATION_EXPIRE_JUPJUP");
            }

            default -> {
                log.error("scheduler_job_failure job={} status=FAILURE reason=UNKNOWN_JOB_TYPE", jobType);
                throw new IllegalArgumentException("알 수 없는 JOB_TYPE: " + jobType);
            }
        }
    }

    private long getDurationMs(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }

    private void shutdown(int exitCode) {
        int code = SpringApplication.exit(context, () -> exitCode);
        System.exit(code);
    }
}