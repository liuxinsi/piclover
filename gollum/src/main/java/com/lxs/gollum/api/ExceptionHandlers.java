package com.lxs.gollum.api;


import com.lxs.gollum.api.vo.Response;
import com.lxs.gollum.api.vo.Status;
import com.lxs.gollum.precious.PreciousException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liuxinsi
 * @date 2018/9/11 15:46
 */
@Log4j2
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(PreciousException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response preciousExceptionHandler(Exception e) {
        log.error("爬虫异常：", e);
        return new Response(Status.ERROR, "服务器异常");
    }
}
