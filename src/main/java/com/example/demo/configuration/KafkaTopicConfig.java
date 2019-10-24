package com.example.demo.configuration;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.connect.util.TopicAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {

        NewTopic compactedTopic =
                TopicAdmin.
                        defineTopic("content-repo")
                        .partitions(1).replicationFactor((short) 1).compacted().build();
        return compactedTopic;
    }

    @Bean
    public NewTopic topic() {

        NewTopic compactedTopic =
                TopicAdmin.defineTopic("item-repo").partitions(1).replicationFactor((short) 1).compacted().build();
        return compactedTopic;
    }

    @Bean
    public NewTopic topic3() {

        NewTopic compactedTopic =
                TopicAdmin.defineTopic("schedule-repo").partitions(1).replicationFactor((short) 1).compacted().build();
        return compactedTopic;
    }

}
