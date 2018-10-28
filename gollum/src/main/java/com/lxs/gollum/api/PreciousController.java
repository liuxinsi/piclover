package com.lxs.gollum.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxs.gollum.Config;
import com.lxs.gollum.api.vo.MetaFile;
import com.lxs.gollum.api.vo.Precious;
import com.lxs.gollum.api.vo.Response;
import com.lxs.gollum.api.vo.Status;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Log4j2
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(path = "/api/precious/v1")
public class PreciousController {
    private final Config config;
    private final ObjectMapper objectMapper;

    @Autowired
    public PreciousController(Config config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/getPrecious")
    public Mono<Response> getPrecious(@RequestParam("id") String id) {
        Pattern pattern = Pattern.compile("^\\d+-\\d+$");
        if (!pattern.matcher(id).matches()) {
            return Mono.just(new Response(Status.ERROR, "没有找到对应的内容"));
        }

        // 加载metafile
        File dir = new File(config.getSaveDir());
        File[] fs = dir.listFiles((dir1, name) -> name.startsWith(id) && name.endsWith(".meta"));
        if (fs == null || fs.length == 0) {
            return Mono.just(new Response(Status.ERROR, "没有找到对应的内容"));
        }

        List<Precious> precious = new ArrayList<>(fs.length);
        try {
            for (File file : fs) {
                // 读取
                MetaFile[] metaFiles;
                try {
                    metaFiles = objectMapper.readValue(file, MetaFile[].class);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    continue;
                }

                Precious p = new Precious();
                for (MetaFile mf : metaFiles) {
                    p.setDescription(mf.getDescription());
                    p.setTitle(mf.getTitle());

                    List<byte[]> imgs = new ArrayList<>(mf.getImgNames().size());
                    for (String imgName : mf.getImgNames()) {
                        File img = new File(config.getSaveDir(), imgName);
                        try (InputStream ins = new FileInputStream(img)) {
                            imgs.add(IOUtils.toByteArray(ins));
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);

                            return Mono.just(new Response(Status.ERROR, "服务器暂时不可用"));
                        }
                    }

                    p.setImage(imgs);
                }

                precious.add(p);
            }
        } finally {
            try {
                clean(fs);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return Mono.just(new Response<>(Status.SUCCESS, precious));
    }

    private void clean(File[] files) throws IOException {
        for (File f : files) {
            MetaFile[] metaFiles;
            try {
                metaFiles = objectMapper.readValue(f, MetaFile[].class);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                continue;
            }

            for (MetaFile metaFile : metaFiles) {
                for (String img : metaFile.getImgNames()) {
                    Files.delete(Paths.get(config.getSaveDir(), img));
                }

                Files.delete(Paths.get(config.getSaveDir(), metaFile.getName()));
            }
        }
    }
}
