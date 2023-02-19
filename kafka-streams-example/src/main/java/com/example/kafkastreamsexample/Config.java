package com.example.kafkastreamsexample;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Config {

  @Bean
  public NewTopic wordsTopic() {
    return TopicBuilder.name("words").build();
  }

  @Bean
  NewTopic vowelsTopic() {
    return TopicBuilder.name("vowels").build();
  }

  @Bean
  NewTopic consonantsTopic() {
    return TopicBuilder.name("consonants").build();
  }
}
