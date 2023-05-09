package com.reason.know.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.reason.know.application.chatgpt.ChatGPTConsumer;
import com.reason.know.application.cmd.WeiChatChatGPTCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@Configuration
@RequiredArgsConstructor
public class Config {

  private final ChatGPTConsumer chatGPTConsumer;


  @Bean
  public EventBus eventBus(
      EventListener eventListener
  ) {
    EventBus eventBus = new EventBus();
    eventBus.register(eventListener);
    return eventBus;
  }

  @Bean
  public EventListener eventListener() {
    return new EventListener(chatGPTConsumer);
  }

  @RequiredArgsConstructor
  static class EventListener {

    private final ChatGPTConsumer chatGPTConsumer;

    @Subscribe
    public void listenInteger(WeiChatChatGPTCmd cmd) {
      chatGPTConsumer.consumer(cmd);
    }
  }
}
