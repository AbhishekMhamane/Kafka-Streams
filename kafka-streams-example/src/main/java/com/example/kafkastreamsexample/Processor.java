package com.example.kafkastreamsexample;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class Processor {

  public Logger log = LoggerFactory.getLogger(Processor.class);

  @Autowired
  public void process(StreamsBuilder builder) {
    
    final Serde<String> stringSerde = Serdes.String();
    final Serde<Long> longSerde = Serdes.Long();

    log.info("Processor processing messages from words topic");

    KStream<String, String> text = builder.stream(
      "words",
      Consumed.with(stringSerde, stringSerde)
    );

    KTable<String, Long> vowelCounts = text
      .filter((k, v) ->
        v.startsWith("a") ||
        v.startsWith("e") ||
        v.startsWith("i") ||
        v.startsWith("o") ||
        v.startsWith("u")
      )
      .mapValues(value -> value.toLowerCase())
      .groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
      .count();

    vowelCounts.toStream().to("vowels", Produced.with(stringSerde, longSerde));

    KTable<String, Long> consonantCounts = text
      .filter((k, v) ->
        !v.startsWith("a") &&
        !v.startsWith("e") &&
        !v.startsWith("i") &&
        !v.startsWith("o") &&
        !v.startsWith("u")
      )
      .mapValues(value -> value.toLowerCase())
      .groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
      .count();

    consonantCounts
      .toStream()
      .to("consonants", Produced.with(stringSerde, longSerde));
  }
}
