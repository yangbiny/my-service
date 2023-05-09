package com.reason.know.api.chatgpt.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "xml")
public class OutMsgEntity {

  // 发送方的账号
  @JacksonXmlProperty(localName = "FromUserName")
  protected String fromUserName = "BrutalHuman";
  // 接收方的账号(OpenID)
  @JacksonXmlProperty(localName = "ToUserName")
  protected String toUserName;
  // 消息创建时间
  @JacksonXmlProperty(localName = "CreateTime")
  protected Long createTime = System.currentTimeMillis();
  /**
   * 消息类型 text 文本消息 image 图片消息 voice 语音消息 video 视频消息 music 音乐消息 news 图文消息
   */
  @JacksonXmlProperty(localName = "MsgType")
  protected String msgType = "text";
  // 文本内容
  @JacksonXmlProperty(localName = "Content")
  private String content;

  public OutMsgEntity(String toUserName, String content) {
    this.toUserName = toUserName;
    this.content = content;
  }
}