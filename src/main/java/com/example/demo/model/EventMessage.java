package com.example.demo.model;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public record EventMessage(String eventId, String eventType) {

}
