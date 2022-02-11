package com.conghuhu.controller;


import com.conghuhu.params.UserPasswordParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.entity.User;
import com.conghuhu.service.UserService;
import com.conghuhu.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
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
    @GetMapping("/currentUser")
    public JsonResult<UserVo> getCurrentUser(@RequestHeader("token") String token) {
        UserVo user = userService.findUserByToken(token);
        if (user != null) {
            return ResultTool.success(user);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "获取当前用户的邀请码", notes = "获取当前用户的邀请码", produces = "application/json")
    @GetMapping("/getInviteCode/{userId}")
    public JsonResult getInviteCode(@PathVariable Long userId) {
        String s = String.valueOf(userId);
        String secret = userService.getInviteCode(s, "cong0917");
        if (!secret.equals("") && secret != null) {
            return ResultTool.success(secret);
        } else {
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "根据邀请码获取邀请者Id", notes = "根据邀请码获取邀请者Id", produces = "application/json")
    @GetMapping("/getUserIdByInviteCode/{inviteCode}")
    public JsonResult<String> getUserIdByInviteCode(@PathVariable String inviteCode) {
        String userId = null;
        userId = userService.getUserIdByInviteCode(inviteCode, "cong0917");
        if (StringUtils.hasText(userId)) {
            return ResultTool.success(userId);
        } else {
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "更改用户密码（无需token）", notes = "更改用户密码", produces = "application/json")
    @PostMapping("/modifyUserPassWord")
    public JsonResult modifyUserPassWord(@RequestBody UserPasswordParam userPasswordParam) {
        return userService.modifyUserPassWord(userPasswordParam);
    }

    @ApiOperation(value = "更改用户状态", notes = "是否为新用户", produces = "application/json")
    @PostMapping("/setNewUserStatus")
    public JsonResult setNewUserStatus(@RequestParam("isNews") Boolean isNews) {
        return userService.setNewUserStatus(isNews);
    }

}
