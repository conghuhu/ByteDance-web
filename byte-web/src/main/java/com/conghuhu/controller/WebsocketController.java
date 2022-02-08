package com.conghuhu.controller;

import com.conghuhu.params.WebsocketParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author conghuhu
 * @create 2022-02-07 15:18
 */
@Api(tags = "websocket测试类")
@RestController
@RequestMapping("/websocket")
public class WebsocketController {
    @Autowired
    private WebSocketService webSocketService;

    @ApiOperation(value = "测试web", notes = "测试web", produces = "application/json")
    @PostMapping("/sendMsgToAllProductUser")
    public JsonResult sendMsgToAllProductUser(@RequestBody WebsocketParam param){
        webSocketService.sendMessageToAll(param.getProductId(),param.getMessage());
        return ResultTool.success();
    }
}
