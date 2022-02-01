package com.conghuhu.controller;

import com.conghuhu.params.RegisterParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author conghuhu
 * @create 2021-10-11 20:38
 */
@Api(tags = "注册类")
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @ApiOperation(value = "注册新用户(无需token)", notes = "注册新用户", produces = "application/json")
    @PostMapping
    public JsonResult register(@RequestBody RegisterParam registerParam) {
        return registerService.register(registerParam);
    }
}
