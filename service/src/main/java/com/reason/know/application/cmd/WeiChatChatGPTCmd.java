package com.reason.know.application.cmd;

import lombok.Data;

/**
 * @author impassive
 */
@Data
public class WeiChatChatGPTCmd {

  private String msgId;

  private String content;

  private String toUser;


}
