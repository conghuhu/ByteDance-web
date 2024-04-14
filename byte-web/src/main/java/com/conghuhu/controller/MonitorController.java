package com.conghuhu.controller;

import com.aliyun.tair.tairts.results.ExtsDataPointResult;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.schedule.ScheduleTask;
import com.conghuhu.utils.JedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author conghuhu
 * @create 2022-03-16 13:42
 */
@Api(tags = "监控")
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @ApiOperation(value = "获取所有在线用户", notes = "获取所有在线用户", produces = "application/json")
    @GetMapping("/getAllActiveUsers")
    public JsonResult<ArrayList<ExtsDataPointResult>> getAllActiveUsers() {
        long curTimeStamp = ScheduleTask.getCurTimeStamp();
        ArrayList<ExtsDataPointResult> results = JedisUtil.getLastTsValue(ScheduleTask.MONITOR, ScheduleTask.ONLINE_USER_COUNT
                , String.valueOf(curTimeStamp - 1000*60*6), String.valueOf(curTimeStamp));
        return ResultTool.success(results);
    }

    @GetMapping("/getAllActiveProductCount")
    public JsonResult getAllActiveProductCount(){
        long curTimeStamp = ScheduleTask.getCurTimeStamp();
        ArrayList<ExtsDataPointResult> results = JedisUtil.getLastTsValue(ScheduleTask.MONITOR, ScheduleTask.ONLINE_PRODUCT_COUNT
                , String.valueOf(curTimeStamp - 1000*60*6), String.valueOf(curTimeStamp));
        return ResultTool.success(results);
    }
}
