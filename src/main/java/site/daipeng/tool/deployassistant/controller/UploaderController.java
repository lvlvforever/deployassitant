package site.daipeng.tool.deployassistant.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lvlvforever on 2020/2/22.
 */
@RestController
@RequestMapping("file")
public class UploaderController {
    @Value("${file.upload.dir}")
    private String dir;

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

}
