package com.airbnb.bookingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig
{
    @Bean
    public NewTopic bookingTopic()
    {
        return new NewTopic(
                "booking-topic",
                1,
                (short) 1
        );
    }

    @Bean
    public NewTopic paymentResultTopic()
    {
        return new NewTopic(
                "payment-result-topic",
                1,
                (short) 1
        );
    }
}