package com.reason.know.api.chatgpt.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class Usage {

  private Integer promptTokens;
  private Integer completionTokens;
  private Integer totalTokens;


}
