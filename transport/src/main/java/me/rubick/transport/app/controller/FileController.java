package me.rubick.transport.app.controller;

import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.utils.Steam;
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
public class FileController {

    private static final String directory = "d:\\uploads";

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

    @RequestMapping(value = "file/{pathName}", method = RequestMethod.GET)
    public void getFile(@PathVariable("pathName") String pathName, HttpServletResponse response) {
        try {
            Document document = documentService.findByPathName(pathName);

            response.setContentType(document.getFileType());
            File file = new File(directory + "\\" + document.getFileName());

            FileInputStream fileInputStream = new FileInputStream(file);

            response.getOutputStream().write(Steam.readStream(fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
