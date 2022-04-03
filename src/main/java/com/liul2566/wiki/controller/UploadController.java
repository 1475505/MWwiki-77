package com.liul2566.wiki.controller;

import com.liul2566.wiki.domain.Filenames;
import com.liul2566.wiki.mapper.FilenamesMapper;
import com.liul2566.wiki.service.FilenamesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ll （ created: 2022-04-03 9:39 )
 */
@RestController
@RequestMapping("/pan")
public class UploadController {
    @Resource
    private FilenamesMapper filenamesMapper;


    @PostMapping("/upload")
    public Map<String, Object> fileupload(MultipartFile file, HttpServletRequest req) throws FileNotFoundException {
        Map<String, Object> result = new HashMap<>();
        String fileName = file.getOriginalFilename();
        File filePath = new File(req.getServletContext().getRealPath("/"));
        File dest = new File(filePath, fileName);
        System.out.println(dest.getAbsoluteFile());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try {
            file.transferTo(dest);
            filenamesMapper.insert(fileName);
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + fileName;
            result.put("status", "OK");
            result.put("name", fileName);
            result.put("url", url);
        } catch (IOException e) {
            result.put("status", "ERROR");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @Resource
    private FilenamesService filenameService;

    @GetMapping("/list")
    public List<Filenames> list() {
        return filenameService.list();
    }
}
