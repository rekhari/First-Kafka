package com.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaHelloWorld {
    public static void main(String[] args) throws Exception {
        final Logger logger = LoggerFactory.getLogger(KafkaHelloWorld.class);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pr4-centos:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> rec = new ProducerRecord<String, String>("TutorialTopic", "Hello World" +i);
            producer.send(rec, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Received New Metadata.\n" +
                                "Topic " + recordMetadata.topic() + "\n" +
                                "Partition:" + recordMetadata.partition() + "\n" +
                                "Offset:" + recordMetadata.offset() + "\n" +
                                "Time Stamp " + recordMetadata.timestamp() + "\n" +
                                "Message: " + rec.value() + rec.key()
                                );


                    } else {
                        logger.info("Error While Producing ", e);
                    }
                }
            });
        }
            producer.close();
//        while(true) {
//            System.out.println();
//        }

//        System.in.read();

        }


}

