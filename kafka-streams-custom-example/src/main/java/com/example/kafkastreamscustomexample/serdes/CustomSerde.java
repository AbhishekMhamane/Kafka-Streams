package com.example.kafkastreamscustomexample.serdes;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import com.example.kafkastreamscustomexample.model.PaymentEvent;

@Component
public class CustomSerde {
    
    public Serde<PaymentEvent> custPaymentEvent() 
    {
        JsonSerializer<PaymentEvent> jsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PaymentEvent> jsonDeserializer = new JsonDeserializer<>(PaymentEvent.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeserializer);
    }
}
