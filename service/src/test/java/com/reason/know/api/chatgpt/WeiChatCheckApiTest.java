package com.reason.know.api.chatgpt;

import com.google.common.eventbus.EventBus;
import com.reason.know.api.chatgpt.form.InMsgEntity;
import com.reason.know.application.chatgpt.ChatGPTConsumer;
import com.reason.know.application.chatgpt.ChatGptAdapter;
import com.reason.know.config.KeyConfig;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author impassive
 */
class WeiChatCheckApiTest {

  private WeiChatCheckApi weiChatCheckApi;

  @BeforeEach
  void setUp() throws IOException {
    KeyConfig keyConfig = new KeyConfig();
    keyConfig.setKeyPath("/Users/reasonknow/conf/chatgpt/key");
    weiChatCheckApi = new WeiChatCheckApi(new ChatGPTConsumer(new EventBus(), new ChatGptAdapter(
        keyConfig.chatGPTKey()), null));
  }

  @Test
  void chatGPT() {
    InMsgEntity entity = new InMsgEntity();
    entity.setContent("test 是什么意思");
    weiChatCheckApi.chatGPT(entity);
  }
}