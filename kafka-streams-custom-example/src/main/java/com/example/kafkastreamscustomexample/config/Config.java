package com.example.kafkastreamscustomexample.config;

import com.example.kafkastreamscustomexample.model.PaymentEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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
  
}
