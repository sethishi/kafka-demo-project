package com.example.demo.listener;

import com.example.demo.configuration.Topics;
import com.example.demo.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.demo.configuration.Topics.*;
import static java.lang.System.out;

@Component
@Slf4j
public class TopicsListener {


    @KafkaListener(topics = {Topics.MMR_ITEM_REPO_TOPIC,MMR_SCHEDULE_REPO_TOPIC,MMR_CONTENT_REPO_TOPIC,COMPACTED_OUTPUT_TOPIC})
    public void listenToItem(ConsumerRecord<String, String> record) {
        out.println(String.format("Message with key %s Received on Topic: %s :: %s", record.key(),record.topic(),record.value()));

    }

    @KafkaListener(topics = "newTopic")
    public void listenTonewTopic(ConsumerRecord<String, String> record) {
        out.println("Message Received on new Topic : " + "newTopic " +
                "" + " >>>> " + record.value() + " with Key " + record.key());

    }

    @KafkaListener(topics = "streamingTopic2")
    public void listenToItemStream(ConsumerRecord<String, Item> record) {
        out.println("Message Received on new Topic : " + "item stream topic " +
                "" + " >>>> " + record.value() + " with Key " + record.key());

    }


}
