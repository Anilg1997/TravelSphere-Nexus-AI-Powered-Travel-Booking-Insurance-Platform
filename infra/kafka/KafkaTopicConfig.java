package com.travelsphere.infra.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean public NewTopic flightsBooked() { return TopicBuilder.name("ts.flights.booked").partitions(3).replicas(1).build(); }
    @Bean public NewTopic flightsCancelled() { return TopicBuilder.name("ts.flights.cancelled").partitions(3).replicas(1).build(); }
    @Bean public NewTopic flightsCheckedIn() { return TopicBuilder.name("ts.flights.checked-in").partitions(3).replicas(1).build(); }
    @Bean public NewTopic hotelsBooked() { return TopicBuilder.name("ts.hotels.booked").partitions(3).replicas(1).build(); }
    @Bean public NewTopic hotelsCancelled() { return TopicBuilder.name("ts.hotels.cancelled").partitions(3).replicas(1).build(); }
    @Bean public NewTopic transportBooked() { return TopicBuilder.name("ts.transport.booked").partitions(3).replicas(1).build(); }
    @Bean public NewTopic carsBooked() { return TopicBuilder.name("ts.cars.booked").partitions(3).replicas(1).build(); }
    @Bean public NewTopic policyIssued() { return TopicBuilder.name("ts.insurance.policy-issued").partitions(3).replicas(1).build(); }
    @Bean public NewTopic claimFiled() { return TopicBuilder.name("ts.insurance.claim-filed").partitions(3).replicas(1).build(); }
    @Bean public NewTopic claimResolved() { return TopicBuilder.name("ts.insurance.claim-resolved").partitions(3).replicas(1).build(); }
    @Bean public NewTopic packagesBooked() { return TopicBuilder.name("ts.packages.booked").partitions(3).replicas(1).build(); }
    @Bean public NewTopic paymentsProcessed() { return TopicBuilder.name("ts.payments.processed").partitions(3).replicas(1).build(); }
    @Bean public NewTopic paymentsFailed() { return TopicBuilder.name("ts.payments.failed").partitions(3).replicas(1).build(); }
    @Bean public NewTopic paymentsRefunded() { return TopicBuilder.name("ts.payments.refunded").partitions(3).replicas(1).build(); }
    @Bean public NewTopic usersRegistered() { return TopicBuilder.name("ts.users.registered").partitions(3).replicas(1).build(); }
    @Bean public NewTopic documentsGenerated() { return TopicBuilder.name("ts.documents.generated").partitions(3).replicas(1).build(); }
    @Bean public NewTopic searchIndexed() { return TopicBuilder.name("ts.search.indexed").partitions(3).replicas(1).build(); }
    @Bean public NewTopic aiQueryLogged() { return TopicBuilder.name("ts.ai.query-logged").partitions(3).replicas(1).build(); }
    @Bean public NewTopic notificationsSend() { return TopicBuilder.name("ts.notifications.send").partitions(3).replicas(1).build(); }
    @Bean public NewTopic fraudAlert() { return TopicBuilder.name("ts.admin.fraud-alert").partitions(3).replicas(1).build(); }
}
