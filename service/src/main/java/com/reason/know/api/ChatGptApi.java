package com.reason.know.api;

import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author impassive
 */
@Controller
public class ChatGptApi {

  private final HttpServletResponse response;

  public ChatGptApi(HttpServletResponse response) {
    this.response = response;
  }

  @PostMapping("/cus/f/w/chat/")
  public void test(){

  }


  /**
   * 微信验证
   *
   * @return
   */
  @GetMapping("/checkSignature")
  public void validate( @RequestParam("signature") String signature,
      @RequestParam("timestamp") String timestamp,
      @RequestParam("nonce") String nonce,
      @RequestParam("echostr") String echostr) {

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
      // 如果一致，返回 echostr 参数的值
      renderToView(echostr, response);
    } else {
      // 如果不一致，返回 "Invalid request" 字符串
      renderToView("Invalid request", response);
    }
  }


  /**
   * @param text: 返回字符串
   *            response: HttpServletResponse 对象
   * @return 去除""的字符串
   * @author Chence
   * @description 用来除去字符串中的双引号
   * @date 2023/3/31 23:17
   */
  public static void renderToView(String text, HttpServletResponse response) {

    try {
      response.setContentType("text/plain;charset=UTF-8");
      response.getWriter().write(text);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
