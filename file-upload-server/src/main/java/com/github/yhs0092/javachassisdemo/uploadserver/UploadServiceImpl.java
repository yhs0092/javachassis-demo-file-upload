package com.github.yhs0092.javachassisdemo.uploadserver;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.config.DynamicProperty;

@RestSchema(schemaId = "fileUpload")
@RequestMapping(path = "/upload")
public class UploadServiceImpl implements UploadService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

  @RequestMapping(path = "/multipartFileUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA)
  @Override
  public String multipartFileUpload(MultipartFile file, String fileName) {
    if (null == file) {
      LOGGER.info("upload file is null, nothing to do");
      return "null upload file";
    }

    LOGGER.info("multipartFileUpload() is called, fileName = [{}], fileSize = [{}]", fileName, file.getSize());
    String saveFileName = getSaveFileName(fileName);

    File savedFile = new File(saveFileName);

    String result = "success";
    try {
      file.transferTo(savedFile);
      LOGGER.info("file saved, filePath = [{}]", savedFile);
    } catch (IOException e) {
      e.printStackTrace();
      result = "failed";
    }

    LOGGER.info("result = [{}]", result);
    return result;
  }

  private String getSaveFileName(String fileName) {
    DynamicProperty saveFileDirProperty = DynamicProperty.getInstance("fileUploadDemo.saveFileDir");
    String saveFileName = saveFileDirProperty.getString();
    if (StringUtils.isEmpty(saveFileName)) {
      throw new InvocationException(Status.BAD_REQUEST, "file name should not be empty");
    }

    if (saveFileName.endsWith(File.separator)) {
      saveFileName = saveFileName + fileName;
    } else {
      saveFileName = saveFileName + File.separator + fileName;
    }

    return saveFileName;
  }
}
