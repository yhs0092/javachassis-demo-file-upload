package com.github.yhs0092.javachassisdemo;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;

public class AppServer {
  public static void main(String[] args) throws Exception {
    System.out.println("Hello World!");
    Log4jUtils.init();
    BeanUtils.init();
  }
}
