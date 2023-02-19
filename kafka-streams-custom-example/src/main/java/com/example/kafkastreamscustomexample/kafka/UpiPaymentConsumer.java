package com.example.kafkastreamscustomexample.kafka;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UpiPaymentConsumer {

  Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

  @KafkaListener(
    topics = "${upi.topic.name}",
    containerFactory = "PaymentEventKafkaListenerContainerFactory"
  )
  public void consumerUpiPayments(ConsumerRecord<String, PaymentEvent> record) {
    log.info(
      "UpiPaymentConsumer received payment event from topic upi-payments with key " +
      record.key() +
      "& value " +
      record.value()
    );
  }
}
