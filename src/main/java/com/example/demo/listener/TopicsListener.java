package com.example.demo.listener;

import com.example.demo.configuration.Topics;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TopicsListener{

    private static final String ITEM_REPO="item-repo";

    @KafkaListener(topics = Topics.MMR_ITEM_REPO_TOPIC)
    public void listenToItem(ConsumerRecord<String, String> record){
        System.out.println("Message Received on Topic : "+ Topics.MMR_ITEM_REPO_TOPIC +" >>>> "+  record.value() +" with Key "+ record.key());
    }

    @KafkaListener(topics = Topics.MMR_CONTENT_REPO_TOPIC)
    public void listenToContent(ConsumerRecord<String, String> record){
        System.out.println("Message Received on Topic : "+ Topics.MMR_CONTENT_REPO_TOPIC +" >>>> "+  record.value() +" with Key "+ record.key());
    }

    @KafkaListener(topics = Topics.ITEM_TABLE_TOPIC)
    public void listenToItemTable(ConsumerRecord<String, String> record){
        System.out.println("Message Received on Topic : "+ Topics.MMR_SCHEDULE_REPO_TOPIC +" >>>> "+  record.value() +" with Key "+ record.key());

    }

    @KafkaListener(topics = "WordsWithCountsTopic")
    public void listenToWordCount(ConsumerRecord<String, String> record){
        System.out.println("Message Received on Topic : "+ "WordsWithCountsTopic " +" >>>> "+  record.value() +" with Key "+ record.key());

    }

}
