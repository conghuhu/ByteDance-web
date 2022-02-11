package com.conghuhu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.entity.User;
import com.conghuhu.params.UserPasswordParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.vo.UserVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2021-09-25
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户名查找用户信息
     *
     * @param name
     * @return
     */
    User getByUserName(String name);

    /**
     * 根据token查询用户信息
     *
     * @param token
     * @return
     */
    UserVo findUserByToken(String token);

    /**
     * 根据id查找userVo
     *
     * @param id
     * @return
     */
    UserVo findUserVoById(Long id);

    /**
     * 根据id生成邀请码
     *
     * @param s
     * @param password
     * @return
     */
    String getInviteCode(String s, String password);

    /**
     * 根据邀请码获取userId
     *
     * @param inviteCode
     * @param password
     * @return
     */
    String getUserIdByInviteCode(String inviteCode, String password);

    /**
     * 更改用户密码
     *
     * @param userPasswordParam
     * @return
     */
    JsonResult modifyUserPassWord(UserPasswordParam userPasswordParam);

    JsonResult setNewUserStatus(Boolean isNews);
}
