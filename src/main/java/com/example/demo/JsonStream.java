package com.example.demo;

import com.example.demo.configuration.Topics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;


//Read the string as Json and extract the key
public class JsonStream {

    public static void main(String[] args) {

//        final Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "csc-in");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
//        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        KStream<String, String> itemStream = builder.stream(Topics.MMR_ITEM_REPO_TOPIC);
//
//        itemStream.

        String myJSONString = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"group-id\": \"group\"\n" +
                "}";

        JsonObject jobj = new Gson().fromJson(myJSONString, JsonObject.class);






    }
}
