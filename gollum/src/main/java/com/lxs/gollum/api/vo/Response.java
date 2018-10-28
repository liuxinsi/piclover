package com.lxs.gollum.api.vo;

import lombok.Data;

/**
 * @author liuxinsi
 * @date 2018/9/11 16:01
 */
@Data
public class Response<T> {
    private Status status;
    private String mesg;
    private T data;

    public Response(Status status, String mesg) {
        this.status = status;
        this.mesg = mesg;
    }

    public Response(Status status, T data) {
        this.status = status;
        this.data = data;
    }
}
