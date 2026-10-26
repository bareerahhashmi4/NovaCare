package com.example.NovaCare.config;

import com.example.NovaCare.service.TriageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final TriageService triageService;

    @Scheduled(fixedRate = 3000)
    public void runDispatcher() {
        triageService.dispatch();
    }
}
