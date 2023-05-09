package com.reason.know.api.chatgpt.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class CompletionChoices {

  private String text;

  private Integer index;

  private String logprobs;

  private String finishReason;

}
