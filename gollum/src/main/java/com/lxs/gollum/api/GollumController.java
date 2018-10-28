package com.lxs.gollum.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lxs.gollum.Config;
import com.lxs.gollum.api.vo.MetaFile;
import com.lxs.gollum.api.vo.Response;
import com.lxs.gollum.api.vo.Status;
import com.lxs.gollum.api.vo.SubmitUrlsResponse;
import com.lxs.gollum.precious.IPrecious;
import com.lxs.gollum.precious.dto.InstagramDTO;
import com.lxs.gollum.utils.DiskUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author liuxinsi
 * @date 2018/9/11 13:18
 */
@Log4j2
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(path = "/api/gollum/v1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GollumController {
    private final IPrecious<InstagramDTO> iPrecious;
    private final Config config;
    private LongAdder idAdder = new LongAdder();
    private final ObjectMapper objectMapper;

    @Autowired
    public GollumController(IPrecious<InstagramDTO> iPrecious, Config config, ObjectMapper objectMapper) {
        this.iPrecious = iPrecious;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/submitUrls")
    public Mono<Response> submitUrls(@Valid @RequestBody List<String> urls) {
        idAdder.increment();
        String id = System.currentTimeMillis() + "-" + idAdder.longValue();

        File img = new File(config.getSaveDir(), id + ".jpg");
        if (!DiskUtils.haveEnoughSpace(img.getParentFile())) {
            log.warn("目录：{}没有足够空间。", img.getParentFile().getAbsolutePath());
            return Mono.just(new Response(Status.ERROR, "服务器磁盘没有足够空间"));
        }

        File meta = new File(config.getSaveDir(), id + ".meta");
        List<MetaFile> metaFiles = new ArrayList<>(urls.size());

        InstagramDTO dto = iPrecious.pic(urls.get(0));

        MetaFile metaFile = new MetaFile();

        try (OutputStream os = new FileOutputStream(img);
             OutputStream mOs = new FileOutputStream(meta)) {
            IOUtils.write(dto.getImage(), os);
            log.debug("保存至目录：{}/{}", img.getPath(), img.exists());

            metaFile.setName(meta.getName());
            metaFile.setTitle(dto.getTitle());
            metaFile.setDescription(dto.getDescription());
            metaFile.setImgNames(Lists.newArrayList(img.getName()));

            metaFiles.add(metaFile);
            objectMapper.writeValue(mOs, metaFiles);
        } catch (IOException e) {
            log.error("保存至目录：{}异常", img.getPath(), e);
            return Mono.just(new Response(Status.ERROR, "保存图片至服务器异常"));
        }

        return Mono.just(new Response<>(Status.SUCCESS, new SubmitUrlsResponse(id)));
    }
}
