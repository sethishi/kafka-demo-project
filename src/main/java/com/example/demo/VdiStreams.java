package com.example.demo;

import com.example.demo.configuration.Topics;
import com.example.demo.model.Item;
import com.example.demo.model.JsonPOJODeserializer;
import com.example.demo.model.JsonPOJOSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class VdiStreams {


    public static void main(String[] args) {

        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<Item> itemSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Item.class);
        itemSerializer.configure(serdeProps, false);

        final Deserializer<Item> itemDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Item.class);
        itemDeserializer.configure(serdeProps, false);

        final Serde<Item> itemSerde = Serdes.serdeFrom(itemSerializer, itemDeserializer);


        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "csc-in");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        final StreamsBuilder builder = new StreamsBuilder();
        KTable<String, String> itemStream = builder.table(Topics.MMR_ITEM_REPO_TOPIC);
        KTable<String, String> contentSource = builder.table(Topics.MMR_CONTENT_REPO_TOPIC);
        KTable<String, String> scheduleSource = builder.table(Topics.MMR_SCHEDULE_REPO_TOPIC);

        KTable<String, String> joined = itemStream.join(contentSource,
                (leftValue, rightValue) -> "item-repo=" + leftValue + ", content-repo=" + rightValue /* ValueJoiner */
        );


        KTable<String, String> finalTable = scheduleSource.join(joined, (leftValue, rightValue) ->
                "schedule::" + leftValue + ", joined-repo::" + rightValue
        );

        finalTable.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.String()));


        Topology topology = builder.build();
//        System.out.println("Topology :" + topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, props);

        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }


}
