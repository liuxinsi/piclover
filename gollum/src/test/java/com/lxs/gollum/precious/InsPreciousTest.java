package com.lxs.gollum.precious;

import com.lxs.gollum.precious.dto.InstagramDTO;
import com.lxs.gollum.precious.impl.InsPrecious;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liuxinsi
 * @date 2018/9/11 11:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsPreciousTest {
    @Autowired
    public IPrecious<InstagramDTO> precious;

    @Test
    public void te() {
//        InstagramDTO d = precious.pic("https://www.instagram.com/p/BnjRVseHKn_/?taken-by=taiga.co");
//        try {
//            IOUtils.write(d.getImage(),new FileOutputStream("e:\\111.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(d);
    }
}
