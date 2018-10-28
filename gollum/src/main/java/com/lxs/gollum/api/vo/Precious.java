package com.lxs.gollum.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Data
public class Precious {
    private String title;
    private List<byte[]> image;
    private String description;
}
