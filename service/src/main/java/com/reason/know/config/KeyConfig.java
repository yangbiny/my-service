package com.reason.know.config;

import com.reason.know.common.AESUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@Configuration
public class KeyConfig {

  @Setter
  @Value("${chat.gpt.openai.key.path}")
  private String keyPath;

  @Bean
  public String chatGPTKey() throws IOException {
    List<String> strings = FileUtils.readLines(new File(keyPath), Charset.defaultCharset());
    return AESUtils.AESDecode(strings.get(0));
  }

}
