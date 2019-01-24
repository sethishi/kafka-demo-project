package com.example.demo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.Acknowledgment;

import java.util.HashMap;
import java.util.Map;

public class KafkaStreamConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new StreamsConfig(props);
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {

        KStream<String, String> stream = kStreamBuilder.stream(Topics.MMR_ITEM_REPO_TOPIC);
        stream.mapValues(post -> post.toString()).to("item-stream");

        return stream;
    }

    @Bean
    public NewTopic kafkaTopicTest() {
        return new NewTopic("streamingTopic2", 1, (short) 1);
    }

    @KafkaListener(topics = "streamingTopic2", containerFactory = "kafkaListenerContainerFactory")
    public void testListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

        String value = consumerRecord.value();

        System.out.println("VALUE: " + value);

        ack.acknowledge();
    }

}


