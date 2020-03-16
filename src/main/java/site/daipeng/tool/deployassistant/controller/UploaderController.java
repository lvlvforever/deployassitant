package site.daipeng.tool.deployassistant.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by lvlvforever on 2020/2/22.
 */
@RestController
@RequestMapping("file")
public class UploaderController {

    @Value("${file.upload.dir}")
    private String dir;

    @Value("${tmpfile.upload.dir}")
    private String tmpDir;

    @CrossOrigin(allowCredentials = "true", maxAge = 3600)
    @PostMapping(value = "/upload")
    public Map<String, Object> uploadMultiFile(MultipartFile file) {

        Map<String, Object> data = new HashMap<>();
        System.err.println(file);
        String fileName = System.currentTimeMillis() + ".jpg";
        File dest = new File(dir + File.separator + fileName);
        try (InputStream inputStream = file.getInputStream()) {
            FileUtils.copyToFile(inputStream, dest);
        } catch (Exception e) {
            System.err.println(e);
        }
        data.put("fileId", fileName);
        return data;
    }
    @CrossOrigin(allowCredentials = "true", maxAge = 3600)
    @PostMapping(value = "/uploadTmpFile")
    public Map<String, Object> uploadTmpFile(MultipartFile file) throws IOException {

        Map<String, Object> data = new HashMap<>();
        String originalName = file.getOriginalFilename();
        String fileName = queryNextFileId() + "." + originalName.substring(originalName.indexOf(".") + 1);

        File dest = new File(tmpDir + File.separator + fileName);
        try (InputStream inputStream = file.getInputStream()) {
            FileUtils.copyToFile(inputStream, dest);
        } catch (Exception e) {
            System.err.println(e);
        }
        data.put("fileId", fileName);
        return data;
    }
    @GetMapping(value = "/queryFiles")
    public List<String> queryFiles() throws IOException {

        List<String> data = new ArrayList<>();
        File file = new File(tmpDir);
        File[] files = file.listFiles();
        if (files == null) {
            return data;

        }
        for (File f : files) {
            String fileName = f.getName();
            data.add(fileName);
        }
        return data;
    }

    private int queryNextFileId() throws IOException {
        File file = new File(tmpDir);
        File[] files = file.listFiles();
        PriorityQueue<Integer> ids = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        if (files == null) {
            return 1;
        }
        for (File f : files) {
            String fileName = f.getName();
            String id = fileName.substring(0, fileName.indexOf("."));
            ids.add(Integer.valueOf(id));
        }
        return ids.peek() + 1;
    }
}
