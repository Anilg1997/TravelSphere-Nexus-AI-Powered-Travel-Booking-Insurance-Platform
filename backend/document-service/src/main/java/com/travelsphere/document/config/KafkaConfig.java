package com.travelsphere.document.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic documentGeneratedTopic() {
        return TopicBuilder.name("ts.documents.generated").partitions(3).replicas(1).build();
    }
}
