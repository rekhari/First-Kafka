package com.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

public class Producer {
    public static void main(String[] args) throws Exception{
        final Logger logger = LoggerFactory.getLogger(Consumer.class);
        String topic = "first_topic";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"pr:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty("acks", "all");
        KafkaProducer<Long,String> kafkaProducer = new KafkaProducer<>(properties);


        Runnable task3 = () -> {
            System.out.println(Thread.currentThread().getName() + " is running");
        };
        Runtime.getRuntime().addShutdownHook(new Thread(task3))  ;
        //RecordMetadata md= (RecordMetadata)kafkaProducer.send(new ProducerRecord(topic, "Message" + UUID.randomUUID())).get();

        for ( int i=0;i<10000000; i++) {
            RecordMetadata md= (RecordMetadata)kafkaProducer.send(new ProducerRecord(topic, "Message" + UUID.randomUUID())).get();
            logger.info("Message sent ==>" + md);
        }
        kafkaProducer.flush();
        kafkaProducer.close();

//        while (true) {
//            RecordMetadata md= (RecordMetadata)kafkaProducer.send(new ProducerRecord(topic, "Message" + UUID.randomUUID())).get();
//            logger.info("Message sent ==>" + md);
//            kafkaProducer.flush();
//        }
    }
}
