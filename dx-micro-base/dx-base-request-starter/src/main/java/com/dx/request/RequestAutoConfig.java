package com.dx.request;

import com.dx.request.config.WebRequestJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 16:11:19
 * @since jdk1.8_202
 */
@Import(value = {
        WebRequestJsonConfig.class
})
@Slf4j
public class RequestAutoConfig {

}
