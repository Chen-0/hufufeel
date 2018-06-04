package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.Document;
import me.rubick.transport.app.repository.DocumentRepository;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DocumentService {

    private static String directory = "d:\\uploads";

    private static String temp_directory;

    static {
        log.info("----------------------------------------------------");
        log.info("操作系统：{}", System.getProperty("os.name"));

        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            directory = "/letv/transport/uploads";
        }

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

        temp_directory = directory + File.separator + "temp";

        File tempFile = new File(temp_directory);
        if (!tempFile.exists()) {
            tempFile.mkdir();
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

    public Document saveAction(Action action) throws IOException {
        String filename = HashUtils.generateString();
        String filetype = "jpg";
        action.getInstance().toFile(directory + File.separator + filename + "." + filetype);

        Document document = new Document();
        document.setDisplayName(filename);
        document.setFileName(filename);
        document.setPathName(filename);
        document.setFileType(filetype);
        document.setCreatedAt(new Date());

        return documentRepository.save(document);
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

    private static File multipartFile2File(MultipartFile multipartFile, String dir, String filename) throws BusinessException {

        if (ObjectUtils.isEmpty(multipartFile) || multipartFile.isEmpty()) {
            throw new BusinessException("[A003]上传文件失败！");
        }
        String filepath = Paths.get(dir, filename).toString();
        File file = new File(filepath);

        try {
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(file));

            stream.write(multipartFile.getBytes());
            stream.close();
        } catch (IOException e) {
            throw new BusinessException("[A003]上传文件失败！");
        }

        return file;
    }

    public static class Action {

        private Thumbnails.Builder<?> builder;

        public Action(MultipartFile multipartFile) throws BusinessException {
            File file = toTempFile(multipartFile);
            this.builder = Thumbnails.of(file);
        }

        public Action(File file) {
            this.builder = Thumbnails.of(file);
        }

        private File toTempFile(MultipartFile multipartFile) throws BusinessException {
            return multipartFile2File(multipartFile, temp_directory, HashUtils.generateString());
        }

        public Action resize(int w, int h) {
            this.builder.sourceRegion(Positions.CENTER, w, h).scale(1.0);
            return this;
        }

        public Action toZip(float q) {
            this.builder.outputQuality(q);
            return this;
        }

        private void toConvert() {
            this.builder.outputFormat("jpg");
        }

        public Thumbnails.Builder<?> getInstance() {
            return builder;
        }

//        public void save() {
//            try {
//                this.builder.toFile(directory + File.separator + HashUtils.generateString() + ".jpg");
//            } catch (IOException e) {
//                log.error("", e);
//            }
//        }
    }
}
