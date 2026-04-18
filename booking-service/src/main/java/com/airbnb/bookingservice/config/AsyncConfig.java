package com.airbnb.bookingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig
{

    @Bean("bookingExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // min threads
        executor.setMaxPoolSize(10);  // max threads
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Booking-");
        executor.initialize();
        return executor;
    }

}
