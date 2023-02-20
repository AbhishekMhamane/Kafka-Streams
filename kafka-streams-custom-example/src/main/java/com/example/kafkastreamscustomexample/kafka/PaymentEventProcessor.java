package com.example.kafkastreamscustomexample.kafka;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import com.example.kafkastreamscustomexample.serdes.CustomSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProcessor {

  Logger log = LoggerFactory.getLogger(PaymentEventProcessor.class);

  @Value("${payment.topic.name}")
  String paymentTopic;

  @Value("${upi.topic.name}")
  String upiTopic;

  @Value("${netbanking.topic.name}")
  String netTopic;

  @Value("${account-balance.topic.name}")
  String accBalanceTopic;

  @Autowired
  CustomSerde customSerde;

  final Serde<String> stringSerde = Serdes.String();
  final Serde<Long> longSerde = Serdes.Long();


  @Autowired
  public void buildPaymentEvent(StreamsBuilder builder) {
    
    log.info("PaymentEventProcessor processing events...");

    KStream<String, PaymentEvent> payStreams = builder.stream(
      paymentTopic,
      Consumed.with(stringSerde, customSerde.custPaymentEvent())
    );

    payStreams.filter((k, v) ->
      v.getMode().equals("upi")
    ).to(upiTopic,Produced.with(stringSerde, customSerde.custPaymentEvent()));

    payStreams.filter((k, v) ->
      v.getMode().equals("netbanking")
    ).to( netTopic,Produced.with(stringSerde, customSerde.custPaymentEvent()));
    
    KTable<String, Long> balanceTable = payStreams
    .groupBy((key,val)->val.getToAccount())
    .aggregate(()->0L,(key,val,total)->total+val.getAmount());

    balanceTable.toStream().to(accBalanceTopic,Produced.with(stringSerde, longSerde));
  }
}

