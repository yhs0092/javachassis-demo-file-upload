package com.github.yhs0092.javachassisdemo.uploadserver;

import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.yhs0092.javachassisdemo.util.FileUploadHelper;

@RestSchema(schemaId = "fileUpload")
@RequestMapping(path = "/upload")
public class UploadServiceImpl implements UploadService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

  @Autowired
  private FileUploadHelper fileUploadHelper;

  @RequestMapping(path = "/multipartFileUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA)
  @Override
  public String multipartFileUpload(@RequestPart(name = "file") MultipartFile file, String fileName) {
    if (null == file) {
      LOGGER.info("upload file is null, nothing to do");
      return "null upload file";
    }

    LOGGER.info("multipartFileUpload() is called, fileName = [{}], fileSize = [{}]", fileName, file.getSize());

    boolean isSaved = fileUploadHelper.saveFromMultipartFile(file, fileName);
    String result = getResultString(isSaved);

    LOGGER.info("result = [{}]", result);
    return result;
  }

  @PostMapping(path = "/uploadMultiFile", consumes = MediaType.MULTIPART_FORM_DATA)
  public String uploadMultiFile(
      @RequestPart(name = "file0") MultipartFile file0,
      @RequestParam(name = "file0Name") String file0Name,
      @RequestPart(name = "file1") Part file1,
      @RequestParam(name = "file1Name") String file1Name,
      @RequestPart(name = "file2") MultipartFile file2,
      @RequestParam(name = "file2Name") String file2Name,
      @RequestPart(name = "file3") Part file3,
      @RequestParam(name = "file3Name") String file3Name) {
    LOGGER.info("file0 = [{}], file0Name = [{}]", file0, file0Name);
    LOGGER.info("file1 = [{}], file1Name = [{}]", file1, file1Name);
    LOGGER.info("file2 = [{}], file2Name = [{}]", file2, file2Name);
    LOGGER.info("file3 = [{}], file3Name = [{}]", file3, file3Name);

    boolean isAllSaved = true;
    isAllSaved &= fileUploadHelper.saveFromMultipartFile(file0, file0Name);
    isAllSaved &= fileUploadHelper.saveFromPart(file1, file1Name);
    isAllSaved &= fileUploadHelper.saveFromMultipartFile(file2, file2Name);
    isAllSaved &= fileUploadHelper.saveFromPart(file3, file3Name);

    String result = getResultString(isAllSaved);
    return result;
  }

  @GetMapping(path = "/throwInvocationException")
  public String throwInvocationException() {
    throw new InvocationException(Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode(),
        Status.REQUEST_ENTITY_TOO_LARGE.getReasonPhrase(), "testErrorData");
  }

  @GetMapping(path = "/throwNullPointerException")
  public String throwNullPointerException() {
    throw new NullPointerException();
  }

  private String getResultString(boolean isSaved) {
    return isSaved ? "success" : "failed";
  }
}
