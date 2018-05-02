package com.github.yhs0092.javachassisdemo.uploadserver;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
  String multipartFileUpload(MultipartFile file, String fileName);
}
