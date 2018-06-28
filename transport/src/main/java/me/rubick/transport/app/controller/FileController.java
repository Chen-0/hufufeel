package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.Steam;
import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.model.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {

    @Resource
    private DocumentService documentService;

    @RequestMapping(value = "file/create", method = RequestMethod.GET)
    public String getUpload() {
        return "file/create";
    }

    @RequestMapping(value = "file/create", method = RequestMethod.POST)
    @ResponseBody
    public String postUpload(@RequestParam("file") MultipartFile multipartFile) {
        documentService.storeDocument(multipartFile);
        return "OK";
    }

    @RequestMapping(value = "file/{pathName:[a-zA-Z0-9.]+}", method = RequestMethod.GET)
    public void getFile(@PathVariable("pathName") String pathName, HttpServletResponse response) {
        try {
            Document document = documentService.findByPathName(pathName);

            response.setContentType(document.getFileType());
            File file = new File(documentService.getDirectory() + File.separator + document.getPathName());

            FileInputStream fileInputStream = new FileInputStream(file);

            response.getOutputStream().write(Steam.readStream(fileInputStream));
        } catch (Exception e) {
            log.info("{}", e.getMessage());
        }
    }
}
