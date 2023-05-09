package com.reason.know.api.chatgpt.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class ChatGPTResp {

  private String id;

  private String object;

  private Long created;

  private String model;

  private List<CompletionChoices> choices;

  private Usage usage;


}
