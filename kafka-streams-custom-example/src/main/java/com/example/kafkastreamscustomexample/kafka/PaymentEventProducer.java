package com.example.kafkastreamscustomexample.kafka;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

  Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

  @Value("${payment.topic.name}")
  String paymentTopic;

  @Autowired
  KafkaTemplate<String, PaymentEvent> paymentTemplate;

  public void sendPaymentEvent(PaymentEvent payEvent) {
    log.info("PaymentEventProducer sending payment event on " + paymentTopic);
    UUID uuid = UUID.randomUUID();
    paymentTemplate.send(paymentTopic, uuid.toString(), payEvent);
  }
}
