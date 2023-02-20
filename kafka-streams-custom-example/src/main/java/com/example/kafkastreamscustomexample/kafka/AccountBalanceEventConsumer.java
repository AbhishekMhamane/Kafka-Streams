package com.example.kafkastreamscustomexample.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceEventConsumer {

  Logger log = LoggerFactory.getLogger(AccountBalanceEventConsumer.class);

  @Value("${account-balance.topic.name}")
  String accBalanceTopic;
  
  @KafkaListener(
    topics = "${account-balance.topic.name}",
    containerFactory = "AccountBalanceEventKafkaListenerContainerFactory"
  )
  void accountBalanceEventConsumer(ConsumerRecord<String, Long> record) {
    log.info(
      "AccountBalanceEventConsumer recieved record from topic "+accBalanceTopic+" with key " +
      record.key() +
      " & with value " +
      record.value()
    );
  }
}
