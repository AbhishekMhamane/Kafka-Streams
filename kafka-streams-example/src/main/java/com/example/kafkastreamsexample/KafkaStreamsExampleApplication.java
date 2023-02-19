package com.example.kafkastreamsexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsExampleApplication {

  static Logger log = LoggerFactory.getLogger(Consumer.class);

  public static void main(String[] args) {
    SpringApplication.run(KafkaStreamsExampleApplication.class, args);
    log.info("Stared Kafka Streams Example");
  }
}
