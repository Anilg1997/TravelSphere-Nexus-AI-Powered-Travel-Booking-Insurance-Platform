package com.travelsphere.hotel.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic hotelBookedTopic() {
        return TopicBuilder.name("ts.hotels.booked").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic hotelCancelledTopic() {
        return TopicBuilder.name("ts.hotels.cancelled").partitions(3).replicas(1).build();
    }
}
