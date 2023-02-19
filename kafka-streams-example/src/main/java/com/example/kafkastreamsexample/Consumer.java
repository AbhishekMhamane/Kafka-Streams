package com.example.kafkastreamsexample;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

  public Logger log = LoggerFactory.getLogger(Consumer.class);

  @KafkaListener(topics = "consonants", groupId = "spring-boot-kafka")
  public void consumeMiddle(ConsumerRecord<String, Long> record) {
    log.info(
      "Consumer consumed messages from consonants topic : " +
      "Received = " +
      record.value() +
      " with key " +
      record.key()
    );
  }

  @KafkaListener(topics = "vowels", groupId = "spring-boot-kafka")
  public void consume(ConsumerRecord<String, Long> record) {
    log.info(
      "Consumer consumed messages from vowels topic : " +
      "Received = " +
      record.value() +
      " with key " +
      record.key()
    );
  }
}
