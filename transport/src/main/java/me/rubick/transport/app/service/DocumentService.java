package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.Document;
import me.rubick.transport.app.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;

@Service
@Slf4j
public class DocumentService {

    private static final String directory = "d:\\uploads";

    static {
        // 启动时检查文件夹是否存在

        File file = new File(directory);

        if (!file.exists()) {
            log.info("{} 不存在，正在创建文件夹", directory);
            if (file.mkdir()) {
                log.info("{} 文件夹创建成功！", directory);
            }
        } else {
            if (!file.isDirectory()) {
                log.info("{} 不是目录，正在删除", directory);
                file.delete();

                log.info("{} 不存在，正在创建文件夹", directory);
                if (file.mkdir()) {
                    log.info("{} 文件夹创建成功！", directory);
                }
            } else {
                log.info("{} 目录存在!", directory);
            }
        }
    }

    @Resource
    private DocumentRepository documentRepository;

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return long id 图片的Id
     */
    public Document storeDocument(MultipartFile multipartFile) {
        try {
            String filename = generateFileName(multipartFile.getOriginalFilename());
            String filepath = Paths.get(directory, filename).toString();

            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));

            stream.write(multipartFile.getBytes());
            stream.close();

            log.info("文件保存成功！正在将信息写入数据库！");
            Document document = new Document();
            document.setFileName(filename);
            document.setFileType(multipartFile.getContentType());
            document.setDisplayName(filename);
            document.setPathName(HashUtils.base64Encode(filename + new Date()));
            document.setCreatedAt(new Date());

            log.info("{} 写入数据库成功！", JSONMapper.toJSON(documentRepository.save(document)));

            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("上传文件失败！");
        return null;
    }

    private String generateFileName(String fileName) {
        String[] string = fileName.split("\\.");
        String suffix = string[string.length - 1];

        String newFileName = HashUtils.generateString() + "." + suffix;
        log.info("随机生成新的文件名，文件名为：{}", newFileName);
        return newFileName;
    }

    public Document findByPathName(String pathName) {
        Document document = documentRepository.findByPathName(pathName);
        log.info("读取数据成功！ document={}", JSONMapper.toJSON(document));
        return document;
    }
}
