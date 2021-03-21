package com.baidetu.common.exception;

import com.baidetu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestControllerAdvice //异步
public class GlobalExceptionHandler {

    /**
     * Shiro异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //返回前端401状态码,源码： UNAUTHORIZED(401, "Unauthorized")
    @ExceptionHandler(value = ShiroException.class) //捕获 Shiro异常
    public Result handler(ShiroException e, HttpServletRequest request) {
        log.error("Shiro访问异常：-------------------------{}", e);
        log.error("访问路径为：", request.getRequestURL());
        return Result.failure(401, e.getMessage(), request.getRequestURL());
    }

    //shiro
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public Result handleShiroException(Exception e) {
        return Result.failure(401, "无权限", null);
    }

    //shiro
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public Result AuthorizationException(Exception e) {
        return Result.failure(401, "权限认证失败", null);
    }

    /**
     * 实体校验异常:校验@NotBlank、@Email这些注解
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //返回前端400状态码,源码：BAD_REQUEST(400, "Bad Request")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：-------------------------{}", e);
        //不像其他的返回e.getMessage()信息了，太长，返回较简短的自定义默认错误信息：类似 @Email(message = "邮箱格式不正确")
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.failure(objectError.getDefaultMessage());
    }

    /**
     * IllegalArgumentException异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //返回前端400状态码,源码：BAD_REQUEST(400, "Bad Request")
    @ExceptionHandler(value = IllegalArgumentException.class) //捕获运行时异常
    public Result handler(IllegalArgumentException e) {
        log.error("Assert(断言)异常：-------------------------{}", e);
        return Result.failure(e.getMessage());
    }

    /**
     * Runtime异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //返回前端400状态码,源码：BAD_REQUEST(400, "Bad Request")
    @ExceptionHandler(value = RuntimeException.class) //捕获运行时异常
    public Result handler(RuntimeException e) {
        log.error("运行时异常：-------------------------{}", e);
        return Result.failure(e.getMessage());
    }

    /**
     * UnsupportedEncodingException异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //返回前端400状态码,源码：BAD_REQUEST(400, "Bad Request")
    @ExceptionHandler(value = UnsupportedEncodingException.class) //捕获运行时异常
    public Result handler(UnsupportedEncodingException e) {
        log.error("编码异常：-------------------------{}", e);
        return Result.failure(e.getMessage());
    }


}
