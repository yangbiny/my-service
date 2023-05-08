package com.reason.know.api.adapter;

import static org.springframework.http.MediaType.TEXT_XML_VALUE;

import com.reason.know.api.chatgpt.form.InMsgEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author impassive
 */
@Slf4j
@RestController
@RequestMapping("r/k/w/")
public class WeiChatCheckApi {

  private final String completionUrl = "https://api.openai.com/v1/completions";

  private final OkHttpClient client = new OkHttpClient.Builder()
      .callTimeout(Duration.ofSeconds(5))
      .build();

  @PostMapping(path = "msg/", consumes = "text/xml;charset=UTF-8")
  public void chatGPT(
      @RequestBody InMsgEntity entity
  ) {
    String content = entity.getContent();
    Request request = new Request.Builder()
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer ")
        .build();
    Call call = client.newCall(request);
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

    System.out.println("xxxxxxxxxxxxxxxxx" + signature);
    log.error("receive info : {}", signature);

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
