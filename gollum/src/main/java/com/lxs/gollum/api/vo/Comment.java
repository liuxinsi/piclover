package com.lxs.gollum.api.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Data
public class Comment {
    @NotEmpty(message = "请填入留言")
    private String comment;
    private String name;
    private String mail;
    private Date date;
}
