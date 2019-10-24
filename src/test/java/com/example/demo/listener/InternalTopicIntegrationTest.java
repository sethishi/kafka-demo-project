package com.example.demo.listener;

import kafka.utils.MockTime;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.test.TestUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
@Ignore
@RunWith(SpringRunner.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        topics = {
                InternalTopicIntegrationTest.STREAMING_TOPIC1,
                InternalTopicIntegrationTest.STREAMING_TOPIC2 })
@TestConfiguration("KafkaStreamsConfiguration")
public class InternalTopicIntegrationTest {

    public static final String STREAMING_TOPIC1 = "testTopic1";
    public static final String STREAMING_TOPIC2 = "testTopic2";


        @Autowired
        private EmbeddedKafkaBroker embeddedKafka;

        @Test
        public void someTest() {
            Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
            consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
            Consumer<String, String> consumer = cf.createConsumer();
            this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, InternalTopicIntegrationTest
                    .STREAMING_TOPIC2);
            ConsumerRecords<String, String> replies = KafkaTestUtils.getRecords(consumer);

            assertThat(replies.count()).isGreaterThanOrEqualTo(1);
        }



    @Configuration
//    @EnableKafkaStreams
    public static class KafkaStreamsConfiguration {

        @Value("${" + EmbeddedKafkaBroker.SPRING_EMBEDDED_KAFKA_BROKERS + "}")
        private String brokerAddresses;

        @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
        public StreamsConfig kStreamsConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddresses);
            return new StreamsConfig(props);
        }
    }


}


