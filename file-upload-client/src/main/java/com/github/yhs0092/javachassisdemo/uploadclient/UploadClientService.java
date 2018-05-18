package com.github.yhs0092.javachassisdemo.uploadclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;

import org.apache.servicecomb.foundation.common.part.FilePart;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.yhs0092.javachassisdemo.uploadserver.UploadServiceIntf;
import com.github.yhs0092.javachassisdemo.util.FileUploadHelper;

@RestSchema(schemaId = "clientConsole")
@RequestMapping(path = "/clientConsole")
public class UploadClientService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadClientService.class);

  @RpcReference(microserviceName = "file-upload-server", schemaId = "fileUpload")
  private UploadServiceIntf uploadService;

  @Autowired
  private FileUploadHelper fileUploadHelper;

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
    String result = null;
    File file1 = new File("D:\\temptmp\\fileUploadDemo\\fileQuiteSmall.txt");
    try (InputStream fis = file.getInputStream()) {
      result = uploadService.multipartFileUpload(fis, fileName);
    }
    LOGGER.info("uploadMultipartFile() result = [{}]", result);
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
      @RequestParam(name = "file3Name") String file3Name) throws IOException {
    LOGGER.info("file0 = [{}], file0Name = [{}]", file0, file0Name);
    LOGGER.info("file1 = [{}], file1Name = [{}]", file1, file1Name);
    LOGGER.info("file2 = [{}], file2Name = [{}]", file2, file2Name);
    LOGGER.info("file3 = [{}], file3Name = [{}]", file3, file3Name);

    File savedFile0 = fileUploadHelper.getFromMultipartFile(file0, file0Name);
    File savedFile1 = fileUploadHelper.getFromPart(file1, file1Name);
    File savedFile3 = fileUploadHelper.getFromPart(file3, file3Name);

    Part part1 = null;
    if (null != savedFile1) {
      part1 = new FilePart(file1Name, savedFile1);
    }

    try (InputStream inputStream = null == file2 ? null : file2.getInputStream()) {
      uploadService.uploadMultiFile(savedFile0, file0Name, part1, file1Name, inputStream, file2Name,
          null == savedFile3 ? null : new FileSystemResource(savedFile3), file3Name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String result = null;
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
