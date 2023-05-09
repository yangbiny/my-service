package com.reason.know.api.chatgpt;

import com.google.common.eventbus.EventBus;
import com.reason.know.api.chatgpt.form.ChatDataRequest;
import com.reason.know.api.chatgpt.form.InMsgEntity;
import com.reason.know.api.chatgpt.vo.ChatGPTResp;
import com.reason.know.api.chatgpt.vo.OutMsgEntity;
import com.reason.know.application.chatgpt.ChatGPTConsumer;
import com.reason.know.application.cmd.WeiChatChatGPTCmd;
import com.reason.know.common.JsonTools;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author impassive
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("r/k/w/")
public class WeiChatCheckApi {

  private final ChatGPTConsumer chatGPTConsumer;


  @PostMapping(path = "msg/", consumes = "text/xml;charset=UTF-8")
  @ResponseBody
  public OutMsgEntity chatGPT(
      @RequestBody InMsgEntity entity
  ) {
    String content = entity.getContent();
    WeiChatChatGPTCmd cmd = new WeiChatChatGPTCmd();
    cmd.setMsgId(entity.getMsgId());
    cmd.setContent(content);
    cmd.setToUser(entity.getFromUserName());
    return chatGPTConsumer.consumer(cmd);
  }

  /**
   * 微信验证
   */
  @GetMapping("msg/")
  public void validate(@RequestParam("signature") String signature,
      @RequestParam("timestamp") String timestamp,
      @RequestParam("nonce") String nonce,
      @RequestParam("echostr") String echostr,
      HttpServletResponse response) {

    //换成自己的token
    String token = "aoNsGxccb677f0DQT1at0Q509X2glnIw";
    // 将 token、timestamp、nonce 三个参数进行字典序排序
    String[] arr = new String[]{token, timestamp, nonce};
    Arrays.sort(arr);
    // 将三个参数字符串拼接成一个字符串进行 sha1 加密
    StringBuilder sb = new StringBuilder();
    for (String str : arr) {
      sb.append(str);
    }
    String encrypted = DigestUtils.sha1Hex(sb.toString());

    // 将加密后的字符串与 signature 进行比较
    if (encrypted.equals(signature)) {
      renderToView(echostr, response);
    } else {
      renderToView("Invalid request", response);
    }
  }

  private void renderToView(String text, HttpServletResponse response) {
    try {
      response.setContentType("text/plain;charset=UTF-8");
      response.getWriter().write(text);
    } catch (Exception e) {
      log.error("renderHas exception ; ", e);
    }
  }

}
