package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
//test
public class ConsumerDemo {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
        String bootstrapServers = "165.227.82.108:9092";
        String groupId = "m4";
        //String topic = "first_topic";
        String topic = "ttt";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers );
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest"); //earliest/latest/none

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        //subscribe consumer to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        //poll for new Data

        while(true){
            ConsumerRecords<String,String> records =
                consumer.poll(Duration.ofMillis(100)); // new in kafka 2.0.0
            for (ConsumerRecord<String,String> record : records) {
                logger.info("Key: "+ record.key() + ", Value: " + record.value());
                logger.info("Partition: "+record.partition() + ", Offset: "+ record.offset() );
            }

        }
    }
}
