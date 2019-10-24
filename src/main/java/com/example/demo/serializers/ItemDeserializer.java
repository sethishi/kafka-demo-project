package com.example.demo.serializers;

import com.example.demo.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.DataInput;
import java.util.Map;

public class ItemDeserializer implements Deserializer<Item> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Item deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        Item item = null;
        try {
            item = mapper.readValue((DataInput) item, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;

    }

    @Override
    public void close() {

    }
}
