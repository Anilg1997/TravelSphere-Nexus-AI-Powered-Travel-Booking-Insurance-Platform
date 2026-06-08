package com.travelsphere.package_.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic packageBookedTopic() {
        return TopicBuilder.name("ts.packages.booked")
                .partitions(3).replicas(1).build();
    }
}
