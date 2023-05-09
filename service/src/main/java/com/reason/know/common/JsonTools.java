package com.reason.know.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author impassive
 */
public class JsonTools {

  private static ObjectMapper mapper = new ObjectMapper();


  public static String toJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readString(String content, Class<T> tClass) {
    try {
      return mapper.readValue(content, tClass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
