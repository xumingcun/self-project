package com.dx.common.vo;

import com.dx.common.enums.HttpStatus;
import lombok.Data;

/**
 *
 * 返回统一类型
 *
 * @author xmc
 * @version 1.0
 * @date 2023/3/5 15:35:24
 * @since jdk17
 */
@Data
public class ResultVO<T>{

    /**
     * 状态码
     */
    private Integer code;


    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 事务追踪id
     */
    private String traceId;


    public ResultVO(HttpStatus status, String msg, T t) {
        this.code = status.value();
        this.message = msg;
        this.data = t;
    }
    public ResultVO(HttpStatus status, String msg) {
        this.code = status.value();
        this.message = msg;
    }

    public ResultVO() {

    }

    public static ResultVO<Void> ok(){
        return new ResultVO<>(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
    }

    public static <R> ResultVO<R> success(R data ){
        return new ResultVO<>(HttpStatus.OK,HttpStatus.OK.getReasonPhrase(),data);
    }

    public static <R> ResultVO<R> success(String message,R data){
        return new ResultVO<>(HttpStatus.OK,message,data);
    }


    public static ResultVO<Void> error(){
        return new ResultVO<>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static <R> ResultVO<R> error(String message,R data){
        return new ResultVO<>(HttpStatus.INTERNAL_SERVER_ERROR,message,data);
    }


    public static <R> ResultVO<R> error(HttpStatus status ,R data){
        return new ResultVO<>(status,status.getReasonPhrase(),data);
    }



}
