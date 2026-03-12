package dev.iamryan.exception;

import dev.iamryan.common.ResponseCode;
import dev.iamryan.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<Void> exceptionHandler(Exception e) {
        LOGGER.error("Exception", e);
        return ResponseResult.error(ResponseCode.EXCEPTION.getCode(), e.getMessage());
    }

    @ExceptionHandler(IpLimitException.class)
    @ResponseBody
    public ResponseResult<Void> ipLimitHandler(IpLimitException e) {
        LOGGER.error("IpLimitException", e);
        return ResponseResult.error(ResponseCode.IP_LIMIT.getCode(), e.getMessage());
    }
}
