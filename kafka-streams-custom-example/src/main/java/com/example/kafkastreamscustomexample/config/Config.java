package com.example.kafkastreamscustomexample.config;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class Config {

  @Value("${spring.kafka.bootstrap-servers}")
  String bootstrapServers;

  @Value("${payment.topic.name}")
  String payTopic;

  @Value("${upi.topic.name}")
  String upiPayTopic;

  @Value("${netbanking.topic.name}")
  String netPayTopic;

  @Value("${account-balance.topic.name}")
  String accBalanceTopic;

  @Bean
  public NewTopic paymentsTopic() {
    return TopicBuilder.name(payTopic).build();
  }

  @Bean
  public NewTopic upiPaymentTopic() {
    return TopicBuilder.name(payTopic).build();
  }

  @Bean
  public NewTopic netPaymentTopic() {
    return TopicBuilder.name(payTopic).build();
  }

  @Bean
  public NewTopic accountBalanceTopic() {
    return TopicBuilder.name(accBalanceTopic).build();
  }

  @Bean
  public ProducerFactory<String, PaymentEvent> paymentProducerFactory() {
    Map<String, Object> props = new HashMap<String, Object>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName()
    );
    props.put(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      JsonSerializer.class.getName()
    );
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, PaymentEvent> getPaymentEventTemplate() {
    return new KafkaTemplate<>(paymentProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, PaymentEvent> paymentConsumerFactory() {
    JsonDeserializer<PaymentEvent> deserializer = new JsonDeserializer<>(
      PaymentEvent.class
    );
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class
    );
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group");

    return new DefaultKafkaConsumerFactory<>(
      props,
      new StringDeserializer(),
      deserializer
    );
  }

  @Bean(name="PaymentEventKafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> ContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(paymentConsumerFactory());
    return factory;
  }
  
  @Bean
  public ConsumerFactory<String, Long> accBalanceConsumerFactory() {

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class
    );
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group");

    return new DefaultKafkaConsumerFactory<>(
      props,
      new StringDeserializer(),
      new LongDeserializer()
    );
  }

  @Bean(name="AccountBalanceEventKafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, Long> accBalanceContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(accBalanceConsumerFactory());
    return factory;
  }

  @Bean(name = 
  KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public KafkaStreamsConfiguration kStreamsConfigs() {
      return new KafkaStreamsConfiguration(Map.of(
          StreamsConfig.APPLICATION_ID_CONFIG, "testStreams",
          StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
          StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
          StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName(),
          StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName()
      ));
  }
}
