package com.example.kafkastreamscustomexample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafkastreamscustomexample.kafka.PaymentEventProducer;
import com.example.kafkastreamscustomexample.model.PaymentEvent;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins="*")
public class Controller {
    
    @Autowired
    PaymentEventProducer payEventProd;

    @GetMapping("/payment")
    ResponseEntity<?> publishPayment(@RequestBody PaymentEvent payEvent)
    {
        payEvent.setPaymentId(UUID.randomUUID().toString());
        payEventProd.sendPaymentEvent(payEvent);
        return new ResponseEntity<String>("paymentEvent triggered",HttpStatus.OK);
    }
}
