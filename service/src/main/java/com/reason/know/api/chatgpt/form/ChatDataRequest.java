package com.reason.know.api.chatgpt.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class ChatDataRequest {

  private String model = "text-davinci-003";

  private String prompt;

  private Integer maxTokens = 3000;

  private Double topP = 1D;

  private Integer n = 1;

  private String suffix = "";

  private Double temperature = 0.2;

}
