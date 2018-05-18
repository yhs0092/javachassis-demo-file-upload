package com.github.yhs0092.javachassisdemo.uploadserver;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.springframework.core.io.Resource;

public interface UploadServiceIntf {
  String multipartFileUpload(InputStream file, String fileName);

  String uploadMultiFile(
      File file0,
      String file0Name,
      Part file1,
      String file1Name,
      InputStream file2,
      String file2Name,
      Resource file3,
      String file3Name);

  String throwInvocationException();

  String throwNullPointerException();
}
