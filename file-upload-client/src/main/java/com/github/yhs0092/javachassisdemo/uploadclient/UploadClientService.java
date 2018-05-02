package com.github.yhs0092.javachassisdemo.uploadclient;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.yhs0092.javachassisdemo.uploadserver.UploadService;

@RestSchema(schemaId = "clientConsole")
@RequestMapping(path = "/clientConsole")
public class UploadClientService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadClientService.class);

  @RpcReference(microserviceName = "file-upload-server", schemaId = "fileUpload")
  private UploadService uploadService;

  @PostMapping(path = "/uploadNull")
  public String uploadNullFile() {
    LOGGER.info("uploadNullFile() is called");
    String result = uploadService.multipartFileUpload(null, "nullFile.txt");
    LOGGER.info("uploadNullFile() result = [{}]", result);
    return result;
  }
}
