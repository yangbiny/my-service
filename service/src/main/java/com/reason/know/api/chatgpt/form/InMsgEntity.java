package com.reason.know.api.chatgpt.form;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "xml")
public class InMsgEntity {

  // 开发者微信号
  @JacksonXmlProperty(localName = "FromUserName")
  protected String fromUserName;
  // 发送方帐号（一个OpenID）
  @JacksonXmlProperty(localName = "ToUserName")
  protected String toUserName;
  // 消息创建时间
  @JacksonXmlProperty(localName = "CreateTime")
  protected Long createTime;
  /**
   * 消息类型 text 文本消息 image 图片消息 voice 语音消息 video 视频消息 music 音乐消息
   */
  @JacksonXmlProperty(localName = "MsgType")
  protected String msgType;
  // 消息id
  @JacksonXmlProperty(localName = "MsgId")
  protected String msgId;
  // 文本内容
  @JacksonXmlProperty(localName = "Content")
  private String content;
  // 图片链接（由系统生成）
  @JacksonXmlProperty(localName = "PicUrl")
  private String picUrl;
  // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
  @JacksonXmlProperty(localName = "MediaId")
  private String mediaId;
}