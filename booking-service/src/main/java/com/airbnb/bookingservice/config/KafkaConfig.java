package com.airbnb.bookingservice.config;

import com.airbnb.bookingservice.DTO.BookingEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig
{

        @Bean
        public ProducerFactory<String, BookingEvent> producerFactory() {
            Map<String, Object> config = new HashMap<>();

            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

            return new DefaultKafkaProducerFactory<>(config);
        }

        @Bean
        public KafkaTemplate<String, BookingEvent> kafkaTemplate()
        {
            return new KafkaTemplate<>(producerFactory());
        }

        @Bean
         public DefaultErrorHandler errorHandler(KafkaTemplate<String, BookingEvent> template)
        {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template);

        FixedBackOff backOff = new FixedBackOff(1000L, 3); // retry 3 times

        return new DefaultErrorHandler(recoverer, backOff);
       }
}
