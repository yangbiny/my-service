package com.reason.know.application.chatgpt;

import com.google.common.eventbus.EventBus;
import com.reason.know.api.chatgpt.vo.OutMsgEntity;
import com.reason.know.application.cmd.WeiChatChatGPTCmd;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTConsumer {

  private final EventBus eventBus;

  private final ChatGptAdapter chatGptAdapter;

  private final ThreadPoolExecutor mainThread;

  public OutMsgEntity consumer(WeiChatChatGPTCmd cmd) {
    String content = cmd.getContent();
    if (StringUtils.startsWith(content, "rs-")) {
      String replace = content.replace("rs-", "");
      OutMsgEntity outMsgEntity = chatGptAdapter.result(replace);
      if (outMsgEntity != null) {
        return outMsgEntity;
      }
      return new OutMsgEntity(cmd.getToUser(),
          "请稍过一会输入 %s 获取结果".formatted(cmd.getContent()));
    }
    mainThread.submit(() -> eventBus.post(cmd));
    return new OutMsgEntity(cmd.getToUser(), "请输入 rs-%s 获取结果".formatted(cmd.getMsgId()));
  }
}
