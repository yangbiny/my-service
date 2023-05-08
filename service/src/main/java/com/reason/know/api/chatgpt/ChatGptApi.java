package com.reason.know.api.chatgpt;

import com.reason.know.api.chatgpt.form.InMsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author impassive
 */
@Slf4j
@RestController
@RequestMapping("/r/k/w/msg/")
public class ChatGptApi {


  @PostMapping("/")
  public void chatGPT(
      @RequestBody InMsgEntity entity
  ) {
    log.error("entity : {}", entity);
  }

}
