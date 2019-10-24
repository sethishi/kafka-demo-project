package com.example.demo;

import com.example.demo.configuration.Topics;
import com.example.demo.model.Item;
import com.example.demo.model.JsonPOJODeserializer;
import com.example.demo.model.JsonPOJOSerializer;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//Read the string as Json and extract the key
public class JsonStream {

    private static final Gson gson= new Gson();

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
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, Item> stream = builder.stream(Topics.MMR_ITEM_REPO_TOPIC, Consumed.with(Serdes.String(), itemSerde));

        KStream<String, Item> rekeyed = stream.
                selectKey((key, value) -> value.getItemId());

        rekeyed.to("newTopic", (Produced.with(Serdes.String(), itemSerde)));

    }

}
