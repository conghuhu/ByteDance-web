package com.conghuhu.controller;


import com.conghuhu.cache.Cache;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.entity.User;
import com.conghuhu.service.UserService;
import com.conghuhu.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2021-09-25
 */
@Api(tags = "用户类")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "查询用户", notes = "查询用户", produces = "application/json")
    @GetMapping("/{id}")
    public JsonResult<User> getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user != null) {
            return ResultTool.success(user);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "根据token查询当前用户", notes = "查询当前用户", produces = "application/json")
    @Cache(expire = 2 * 60 * 1000, name = "currentUser")
    @GetMapping("/currentUser")
    public JsonResult<User> getCurrentUser(@RequestHeader("token") String token) {
        User user = userService.findUserByToken(token);
        if (user != null) {
            return ResultTool.success(user);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }


}
