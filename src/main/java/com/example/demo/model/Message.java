package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Message {

  private String message;
  private String name;

  public Message(String message, String name) {
    this.message = message;
    this.name = name;
  }
}
