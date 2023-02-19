package com.example.kafkastreamsexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/publish")
public class Controller {

  @Autowired
  Producer producer;

  @GetMapping("/{word}")
  public ResponseEntity<?> sendMsg(@PathVariable String word) {
    producer.sendMessage(word);
    return new ResponseEntity<String>("Published message", HttpStatus.OK);
  }
}
