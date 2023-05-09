package com.example.demo.broker.consumer;

import com.example.demo.model.Task;
import com.example.demo.utils.ObjectUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * TaskDeserializer.
 */
@Slf4j
public class TaskDeserializer implements Deserializer<Task> {

  @Override
  public Task deserialize(String s, byte[] bytes) {
    try {
      return ObjectUtil.OBJECT_MAPPER.readValue(bytes, Task.class);
    } catch (IOException e) {
      log.error("Error deserializing for topic " + s + " [" + e.getLocalizedMessage() + "]");
    }
    return null;
  }

}
