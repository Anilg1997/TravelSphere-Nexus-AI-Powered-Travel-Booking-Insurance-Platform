package com.travelsphere.flight.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic flightBookedTopic() {
        return TopicBuilder.name("ts.flights.booked").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic flightCancelledTopic() {
        return TopicBuilder.name("ts.flights.cancelled").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic flightCheckedInTopic() {
        return TopicBuilder.name("ts.flights.checked-in").partitions(3).replicas(1).build();
    }
}
