package com.reason.know.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.reason.know.application.chatgpt.ChatGPTConsumer;
import com.reason.know.application.chatgpt.ChatGptAdapter;
import com.reason.know.application.cmd.WeiChatChatGPTCmd;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@Configuration
@RequiredArgsConstructor
public class Config {

  private final ChatGptAdapter chatGPTConsumer;

  @Bean
  public ThreadPoolExecutor mainThread() {
    return new ThreadPoolExecutor(
        2, 2, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10)
    );
  }


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

    private final ChatGptAdapter chatGptAdapter;

    @Subscribe
    public void listenInteger(WeiChatChatGPTCmd cmd) {
      chatGptAdapter.consumer(cmd);
    }
  }
}
