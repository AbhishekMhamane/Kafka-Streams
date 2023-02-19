package com.example.kafkastreamsexample;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

  public Logger log = LoggerFactory.getLogger(Producer.class);

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String msg) {
    log.info("Sending message on kafka from producer to words topic : " + msg);
    UUID uuid = UUID.randomUUID();
    kafkaTemplate.send("words", uuid.toString(), msg);
  }
}
