package com.github.yhs0092.javachassisdemo.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Part;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.config.DynamicProperty;

@Component
public class FileUploadHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadHelper.class);

  public boolean saveFromPart(Part file, String fileName) {
    File result = getFromPart(file, fileName);
    return null != result;
  }

  public File getFromPart(Part file, String fileName) {
    if (null == file) {
      return null;
    }

    File result = null;
    try {
      String saveFileName = getSaveFileName(fileName);
      file.write(saveFileName);
      result = new File(saveFileName);
      LOGGER.info("file saved, filePath = [{}]", saveFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public boolean saveFromMultipartFile(MultipartFile file, String fileName) {
    File result = getFromMultipartFile(file, fileName);
    return null != result;
  }

  public File getFromMultipartFile(MultipartFile file, String fileName) {
    if (null == file) {
      return null;
    }

    File result = null;
    File savedFile = getSavedFile(fileName);
    try {
      file.transferTo(savedFile);
      LOGGER.info("file saved, filePath = [{}]", savedFile);
      result = savedFile;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  private File getSavedFile(String fileName) {
    String saveFileName = getSaveFileName(fileName);

    return new File(saveFileName);
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
