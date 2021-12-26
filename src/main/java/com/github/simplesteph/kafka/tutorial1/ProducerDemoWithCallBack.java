package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    public static void main(String[] args) {
        String bootstrapServers = "165.227.82.108:9092";

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

        //create producer properties
        Properties properties = new Properties();
                properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
                properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        // create the producer

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // create producer record
        for (int i=0; i<10; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("first_topic", "hello world" + Integer.toString(i));

            // send data - asynchronous
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //execute every time a record is successfully send or an exception is thrown
                    if (e == null) {
                        // the record was successfully send
                        logger.info("Received new Metadata. \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "TimeStamp: " + recordMetadata.timestamp());

                    } else {
                        logger.error("Error while producing");

                    }
                }
            });
        }
        //flush data
        producer.flush();

        //flush and close producer

        producer.close();

    }
}
