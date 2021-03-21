package com.baidetu.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code; //响应状态码，200正常，其余异常

    private String msg; //响应信息

    private Object data; //响应数据封装


    /**
     * 定义成功方法:需传3个参数
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */

    public static Result success(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    //重载成功方法(确定成功状态下):需传data参数
    public static Result success(Object data) {
        return success(200, "操作成功", data);
    }

    /**
     * 定义失败方法，需传3个参数
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static Result failure(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    //重载失败方法:确定状态码，传余下两个参数即可
    public static Result failure(String msg, Object data) {
        return failure(400, msg, data);
    }

    //重载失败方法:确定状态码，并且不返回参数，传错误信息即可
    public static Result failure(String msg) {
        return failure(400, msg, null);
    }

}
