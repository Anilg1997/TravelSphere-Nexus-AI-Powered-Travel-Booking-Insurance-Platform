package com.travelsphere.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic loyaltyUpdatedTopic() {
        return TopicBuilder.name("ts.users.loyalty-updated")
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic userRegisteredTopic() {
        return TopicBuilder.name("ts.users.registered")
                .partitions(3).replicas(1).build();
    }
}
