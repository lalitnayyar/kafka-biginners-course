package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithKey {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String bootstrapServers = "165.227.82.108:9092";

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKey.class);

        //create producer properties
        Properties properties = new Properties();
                properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
                properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        // create the producer

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // create producer record
        for (int i=0; i<10; i++) {
            String topic = "first_topic";
            String value = "hello world" + Integer.toString(i);
            String key = "id_"+Integer.toString(i);
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(topic,key,value);
                logger.info("key: "+ key); //log the key
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
            }).get(); //block the .send() to make it synchronous - don't do this in production !
        }
        //flush data
        producer.flush();

        //flush and close producer

        producer.close();

    }
}
