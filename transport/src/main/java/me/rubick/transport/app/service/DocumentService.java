package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.NotFoundException;
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
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
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
        log.info("临时目录：{}", temp_directory);

        File tempFile = new File(temp_directory);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
    }

    public String getDirectory() {
        return directory;
    }

    @Resource
    private DocumentRepository documentRepository;

    public Document uploadProductImage(MultipartFile multipartFile) throws BusinessException {
        Action action = new Action(multipartFile);
        action.resize(200, 200).toConvert();

        try {
            return saveAction(action);
        } catch (IOException e) {
            log.error("", e);
        }

        return null;
    }

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
            document.setName(filename);
            document.setFileType(multipartFile.getContentType());
            document.setPathName(filename);
            document.setCreatedAt(new Date());
            document.setOriginalFilename(multipartFile.getOriginalFilename());

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
        document.setName(filename + "." + filetype);
        document.setPathName(filename + "." + filetype);
        document.setFileType("image/jpeg");
        document.setCreatedAt(new Date());
        document.setOriginalFilename(action.getOriginalFilename());

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
        return documentRepository.findByName(pathName);
    }

    private static File multipartFile2File(MultipartFile multipartFile, String dir, String filename) throws BusinessException {

        if (ObjectUtils.isEmpty(multipartFile) || multipartFile.isEmpty()) {
            throw new BusinessException("[A003]上传文件失败！");
        }


        String[] oldName = multipartFile.getOriginalFilename().split("\\.");
        String filepath = Paths.get(dir, filename).toString() + "." + oldName[oldName.length - 1];
        File file = new File(filepath);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("", e);
            throw new BusinessException("[A003]上传文件失败！");
        }

        return file;
    }

    public File multipartFile2File(MultipartFile multipartFile) throws BusinessException {
        return multipartFile2File(multipartFile, temp_directory, HashUtils.generateString());
    }

    public static class Action {

        private Thumbnails.Builder<?> builder;

        private String originalFilename;

        public Action(MultipartFile multipartFile) throws BusinessException {
            File file = toTempFile(multipartFile);
            check(file);
            this.builder = Thumbnails.of(file);
            setOriginalFilename(multipartFile.getOriginalFilename());
        }

        public Action(File file) {
        }

        private boolean check(File file) throws BusinessException {
            log.info("{}", file.getAbsolutePath());
            String mimetype = new MimetypesFileTypeMap().getContentType(file);
            String type = mimetype.split("/")[0];
            if (!type.equals("image")) {
                throw new BusinessException("文件不是图片文件或格式不对，请上传 JPG 或 PNG 文件！");
            }
            return true;
        }

        private File toTempFile(MultipartFile multipartFile) throws BusinessException {
            return multipartFile2File(multipartFile, temp_directory, HashUtils.generateString());
        }

        public Action resize(int w, int h) {
            this.builder.size(w, h);
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

        public String getOriginalFilename() {
            return originalFilename;
        }

        public void setOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
        }
    }

    public Document findOne(Long id) throws NotFoundException {
        if (ObjectUtils.isEmpty(id)) {
            throw new NotFoundException();
        }

        Document document = documentRepository.findOne(id);

        if (ObjectUtils.isEmpty(document)) {
            throw new NotFoundException();
        }

        return document;
    }
}
