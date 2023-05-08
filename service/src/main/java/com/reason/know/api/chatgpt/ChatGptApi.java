package com.reason.know.api.chatgpt;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

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
@RequestMapping("/r/k/w/")
public class ChatGptApi {


  @PostMapping(params = "msg/", produces = TEXT_XML_VALUE, consumes = TEXT_XML_VALUE)
  public void chatGPT(
      @RequestBody InMsgEntity entity
  ) {
    System.out.println("yyyyyy-xxxxx" + entity.toString());
    log.error("entity : {}", entity);
  }

}
