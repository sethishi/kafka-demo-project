package com.example.demo.listener;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.streams.test.OutputVerifier;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

@Ignore
public class TestUsingTopologyTestDriver {



    private TopologyTestDriver topologyTestDriver ;

    private static StringDeserializer stringDeserializer = new StringDeserializer();
    private static LongDeserializer longDeserializer = new LongDeserializer();

    ConsumerRecordFactory<String, Long> recordFactory = new ConsumerRecordFactory<>(new StringSerializer(), new LongSerializer());


    @Before
    public void setUp() throws Exception {
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream("input-topic").to("result-topic");
        Topology topology = builder.build();

// setup test driver
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        topologyTestDriver = new TopologyTestDriver(topology, config);
    }

    @After
    public void tearDown() {
        topologyTestDriver.close();
    }

    @Test
    public void test1() {

        topologyTestDriver.pipeInput(recordFactory.create("input-topic", "a", 1L, 9999L));

        OutputVerifier.compareKeyValue(topologyTestDriver.readOutput("result-topic", stringDeserializer,longDeserializer), "a", 21L);
        Assert.assertNull(topologyTestDriver.readOutput("result-topic", stringDeserializer, longDeserializer));

    }
}
