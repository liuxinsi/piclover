package com.lxs.gollum.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Data
public class GetPreciousResponse {
    private String id;
    private List<Precious> precious;
}
