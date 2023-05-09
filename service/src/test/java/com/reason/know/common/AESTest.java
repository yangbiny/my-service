package com.reason.know.common;

import org.junit.jupiter.api.Test;

/**
 * @author impassive
 */
public class AESTest {

  @Test
  void test() {
    String content = AESUtils.AESEncode("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");
    System.out.println(content);
    String s = AESUtils.AESDecode(content);
    System.out.println(s);
  }
}
