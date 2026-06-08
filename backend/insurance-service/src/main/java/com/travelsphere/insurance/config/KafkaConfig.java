package com.travelsphere.insurance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean public NewTopic policyIssuedTopic() {
        return TopicBuilder.name("ts.insurance.policy-issued").partitions(3).replicas(1).build();
    }

    @Bean public NewTopic claimFiledTopic() {
        return TopicBuilder.name("ts.insurance.claim-filed").partitions(3).replicas(1).build();
    }

    @Bean public NewTopic claimResolvedTopic() {
        return TopicBuilder.name("ts.insurance.claim-resolved").partitions(3).replicas(1).build();
    }
}
