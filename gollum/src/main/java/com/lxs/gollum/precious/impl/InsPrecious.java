package com.lxs.gollum.precious.impl;

import com.lxs.gollum.precious.IPrecious;
import com.lxs.gollum.precious.PreciousException;
import com.lxs.gollum.precious.dto.InstagramDTO;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @author liuxinsi
 * @date 2018/9/11 9:33
 */
@Log4j2
@Component("ins")
public class InsPrecious implements IPrecious<InstagramDTO> {

    @Override
    public InstagramDTO pic(String url) {
        Connection con = Jsoup.connect(url);
//        con.proxy("127.0.0.1", 51004);

        Document doc;
        try {
            doc = con.get();
        } catch (IOException e) {
            throw new PreciousException("访问instagram异常", e);
        }

        InstagramDTO dto = new InstagramDTO();
        doc.select("meta").forEach(element -> {
            String pro = element.attr("property");
            if (!pro.startsWith("og:")) {
                return;
            }

            String content = element.attr("content");
            switch (pro) {
                case "og:title":
                    dto.setTitle(content);
                    break;
                case "og:image":
                    dto.setImage(download(content));
                    break;
                case "og:description":
                    dto.setDescription(content);
                    break;
                default:
                    break;
            }
        });

        return dto;
    }
}
