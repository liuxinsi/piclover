package com.lxs.gollum.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Data
public class MetaFile {
    private String name;
    private List<String> imgNames;
    private String title;
    private String description;
}
