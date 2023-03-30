package com.dx.management.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.dx.common.vo.ResultVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import com.dx.common.enums.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 16:01:15
 * @since jdk1.8_202
 */
@Log4j2
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private static final ThreadLocal<Map<String,Object>> exceptionHandlerResult = new ThreadLocal<>();


    @Override
    public Mono<Void> handle( ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String body = "Internal Server Error";
        //TODO 按照异常类型进行处理

        /*封装响应体*/
        Map<String, Object> result = new HashMap<>(8);
        result.put("code", status);
        result.put("body", body);

        /*错误记录*/
        ServerHttpRequest request = exchange.getRequest();
        log.error("[全局异常处理] 异常处理请求路径: {}, 记录信息: {}", request.getPath(), ex);

        if (exchange.getResponse().isCommitted()){
            return Mono.error(ex);
        }
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        exceptionHandlerResult.set(result);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
                .route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap(h->h.handle(newRequest))
                .flatMap(res->write(exchange, res));
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        ResultVO resultVO = ResultVO.error(HttpStatus.INTERNAL_SERVER_ERROR,result.get("body").toString());
        if (ObjUtil.equals(result.get("code"), HttpStatus.FOUND.value())) {
            resultVO = ResultVO.error(com.dx.common.enums.HttpStatus.FOUND,result.get("body").toString());
        }
        resultVO.setTraceId(IdUtil.fastSimpleUUID());

        return ServerResponse
                .status(resultVO.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resultVO));
        //.body(BodyInserters.fromObject(resultDTO));
    }

    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return null;
    }
}
