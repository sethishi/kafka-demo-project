package com.example.demo.controller;

import com.example.demo.configuration.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicsController {


    static final String ITEM_REPO="item-repo";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/item/{key}")
    public void itemRepo(@RequestBody String item, @PathVariable String key){
      kafkaTemplate.send(Topics.MMR_ITEM_REPO_TOPIC,key,item);
    }
    @PostMapping("/content/{key}")
    public void contentRepo(@RequestBody String item, @PathVariable String key){

        kafkaTemplate.send(Topics.MMR_CONTENT_REPO_TOPIC,key,item);

    }
    @PostMapping("/schedule/{key}")
    public void scheduleRepo(@RequestBody String item, @PathVariable String key){
        kafkaTemplate.send(Topics.MMR_SCHEDULE_REPO_TOPIC,key,item);

    }
}
