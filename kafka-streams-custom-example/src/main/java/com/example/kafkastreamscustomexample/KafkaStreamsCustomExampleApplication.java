package com.example.kafkastreamscustomexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableKafka
@EnableKafkaStreams
@SpringBootApplication
public class KafkaStreamsCustomExampleApplication {

  static Logger log = LoggerFactory.getLogger(
    KafkaStreamsCustomExampleApplication.class
  );

  public static void main(String[] args) {
    SpringApplication.run(KafkaStreamsCustomExampleApplication.class, args);
    log.info("Kafka Streams Custom Example Application started");
  }
}
