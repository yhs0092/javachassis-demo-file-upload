package com.github.yhs0092.javachassisdemo.uploadclient;

import java.io.IOException;
import java.io.InputStream;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.github.yhs0092.javachassisdemo.uploadserver.UploadServiceIntf;

@RestSchema(schemaId = "clientConsole")
@RequestMapping(path = "/clientConsole")
public class UploadClientService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadClientService.class);

  @RpcReference(microserviceName = "file-upload-server", schemaId = "fileUpload")
  private UploadServiceIntf uploadService;

  @PostMapping(path = "/uploadNull")
  public String uploadNullFile() {
    LOGGER.info("uploadNullFile() is called");
    String result = uploadService.multipartFileUpload(null, "nullFile.txt");
    LOGGER.info("uploadNullFile() result = [{}]", result);
    return result;
  }

  @PostMapping(path = "/uploadMultipartFile")
  public String uploadMultipartFile(MultipartFile file, String fileName) throws IOException {
    LOGGER.info("uploadMultipartFile() is called, fileName = [{}], fileSize = [{}]", fileName, file.getSize());
    String result;
    try (InputStream fis = file.getInputStream()) {
      result = uploadService.multipartFileUpload(fis, fileName);
    }
    LOGGER.info("uploadMultipartFile() result = [{}]", result);
    return result;
  }

  @RequestMapping(path = "/throwInvocationException", method = RequestMethod.GET)
  public String throwInvocationException() {
    return uploadService.throwInvocationException();
  }
  @RequestMapping(path = "/throwNullPointerException", method = RequestMethod.GET)
  public String throwNullPointerException() {
    return uploadService.throwNullPointerException();
  }
}
