package com.example.demo.configuration;

import com.example.demo.model.Item;
import com.example.demo.model.JsonPOJODeserializer;
import com.example.demo.model.JsonPOJOSerializer;
import com.example.demo.serializers.ItemDeserializer;
import com.example.demo.serializers.ItemSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME;

//@Configuration()
//@EnableKafkaStreams
//@EnableKafka
public class BcQKafkaStreamConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Serde<Item> itemSerde;

    @Bean(name = DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamConfig() {

        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<Item> itemSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Item.class);
        itemSerializer.configure(serdeProps, false);

        final Deserializer<Item> itemDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Item.class);
        itemDeserializer.configure(serdeProps, false);

         itemSerde = Serdes.serdeFrom(itemSerializer,itemDeserializer);


        Map<String, Object> props = new HashMap<>();
        props.put("application.id", "testStreams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerializer.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringDeserializer.class);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean(name = "itemStreamBean")
    public KStream<String, Item> itemStreamBean(StreamsBuilder kStreamBuilder) {

        KStream<String, Item> stream = kStreamBuilder.stream(Topics.MMR_ITEM_REPO_TOPIC, Consumed.with(Serdes.String(),itemSerde));
        stream.
                selectKey((key, value) -> value.getItemId()).to("streamingTopic2");
        // Fluent KStream API
        return stream;
    }

    @Bean
    public NewTopic kafkaTopicTest() {
        return new NewTopic("streamingTopic2", 1, (short) 1);
    }

//    @Bean
//    @Primary
//    public StreamsBuilderFactoryBean kStreamBuilder(KafkaStreamsConfiguration streamsConfig) {
//        return new StreamsBuilderFactoryBean(streamConfig());
//    }


}


