package com.example.kafkastreamscustomexample.kafka;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NetBankingConsumer {

  Logger log = LoggerFactory.getLogger(NetBankingConsumer.class);

  @KafkaListener(
    topics = "${netbanking.topic.name}",
    containerFactory = "PaymentEventKafkaListenerContainerFactory"
  )
  public void netBankingEventConsumer(ConsumerRecord<String, PaymentEvent> record) {
    log.info(
      "NetBankingEventConsumer received payment event from netbanking-payments with key " +
      record.key() +
      " & value " +
      record.value()
    );
  }
}
