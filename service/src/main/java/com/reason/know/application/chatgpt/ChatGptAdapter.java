package com.reason.know.application.chatgpt;

import com.reason.know.api.chatgpt.form.ChatDataRequest;
import com.reason.know.api.chatgpt.vo.ChatGPTResp;
import com.reason.know.api.chatgpt.vo.OutMsgEntity;
import com.reason.know.application.cmd.WeiChatChatGPTCmd;
import com.reason.know.common.JsonTools;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGptAdapter {

  private static final Map<String, OutMsgEntity> map = new ConcurrentHashMap<>();

  private final String chatGPTKey;
  private static final String COMPLETION_URL = "https://api.openai.com/v1/completions";
  private final OkHttpClient client = new OkHttpClient.Builder()
      .callTimeout(Duration.ofSeconds(30))
      //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1081)))
      .build();

  public OutMsgEntity result(String msgId) {
    OutMsgEntity outMsgEntity = map.get(msgId);
    if (outMsgEntity != null){
      map.remove(msgId);
    }
    return outMsgEntity;
  }

  public void consumer(WeiChatChatGPTCmd cmd) {
    ChatDataRequest body = new ChatDataRequest();
    body.setPrompt(cmd.getContent());
    okhttp3.RequestBody text = okhttp3.RequestBody.create(JsonTools.toJson(body).getBytes(
        StandardCharsets.UTF_8));
    Request request = new Request.Builder()
        .url(COMPLETION_URL)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer %s".formatted(chatGPTKey))
        .post(text)
        .build();
    OutMsgEntity outMsgEntity;
    try {
      Response response = client.newCall(request).execute();
      if (response.code() != 200) {
        log.error("requesu has failed : {}", response.body().string());
      }
      String respText = response.body().string();
      ChatGPTResp chatGPTResp = JsonTools.readString(respText, ChatGPTResp.class);
      log.info("resp : {}", chatGPTResp);
      outMsgEntity = new OutMsgEntity(cmd.getToUser(),
          chatGPTResp.getChoices().get(0).getText());
    } catch (Exception e) {
      log.error("failed :", e);
      outMsgEntity = new OutMsgEntity(cmd.getToUser(), "真尴尬，失败了，哈哈哈哈哈");
    }
    map.put(cmd.getMsgId(), outMsgEntity);
  }
}
