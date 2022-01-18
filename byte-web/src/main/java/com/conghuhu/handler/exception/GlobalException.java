package com.conghuhu.handler.exception;


import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 对加了RestController注解的方法进行拦截，是一个AOP的实现
 * @author conghuhu
 * @create 2021-09-24 20:45
 */
@RestControllerAdvice
public class GlobalException {
    /**
     * 进行异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public JsonResult exception(Exception e){
        e.printStackTrace();
        return ResultTool.fail();
    }
}
