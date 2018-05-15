package com.github.yhs0092.javachassisdemo.uploadserver;

import java.io.InputStream;

public interface UploadServiceIntf {
  String multipartFileUpload(InputStream file, String fileName);

  String throwInvocationException();

  String throwNullPointerException();
}
