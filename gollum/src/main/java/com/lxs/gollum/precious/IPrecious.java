package com.lxs.gollum.precious;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import java.io.IOException;


/**
 * @author liuxinsi
 * @date 2018/9/11 9:30
 */
@Component
public interface IPrecious<R> {
    R pic(String url);

    default byte[] download(String url) {
        try {
            return Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
        } catch (IOException e) {
            throw new PreciousException("下载异常", e);
        }
    }
}
