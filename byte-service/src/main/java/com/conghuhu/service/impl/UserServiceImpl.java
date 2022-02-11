package com.conghuhu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.User;
import com.conghuhu.mapper.UserMapper;
import com.conghuhu.params.UserPasswordParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.*;
import com.conghuhu.vo.UserVo;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2021-09-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final RedisUtil redisUtil;

    public UserServiceImpl(UserMapper userMapper, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public User getByUserName(String name) {
        return userMapper.getByUserName(name);
    }

    @Override
    public UserVo findUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userName;
        try {
            userName = JwtTokenUtil.getUserNameFromToken(token);
        } catch (ExpiredJwtException e) {
            throw new AccountExpiredException("token失效");
        }

        User userJson = (User) redisUtil.get("Token_" + userName);
        if (userJson == null) {
            User user = getByUserName(userName);
            if (user != null) {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                return userVo;
            }
            return null;
        } else {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userJson, userVo);
            return userVo;
        }
    }

    @Override
    public UserVo findUserVoById(Long id) {
        User user = userMapper.selectById(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public String getInviteCode(String s, String password) {
        byte[] bytes = AESUtil.encrypt(s, password);
        String secret = HexConversion.parseByte2HexStr(bytes != null ? bytes : new byte[0]);
        return secret;
    }

    @Override
    public String getUserIdByInviteCode(String inviteCode, String password) {
        byte[] inviteCodeBytes = HexConversion.parseHexStr2Byte(inviteCode);
        byte[] decrypt = AESUtil.decrypt(inviteCodeBytes, password);
        String userId = new String(decrypt);
        return userId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult modifyUserPassWord(UserPasswordParam userPasswordParam) {
        String username = userPasswordParam.getUsername();
        String password = userPasswordParam.getPassword();
        String verifyCode = userPasswordParam.getVerifyCode();

        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(verifyCode)) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        if (!ValidateUtil.validateEmail(username)) {
            return ResultTool.fail(ResultCode.PARAMS_ERROR);
        }

        User user = userMapper.getByUserName(username);
        if (user == null) {
            return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }

        String realCode = (String) redisUtil.get(username + "_code");
        if (!verifyCode.equals(realCode)) {
            return ResultTool.fail(ResultCode.MAIL_CODE_ERROR);
        }

        User userUpdate = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String passHash = encoder.encode(password);
        userUpdate.setPassword(passHash);

        int update = userMapper.update(userUpdate, new LambdaUpdateWrapper<User>()
                .eq(User::getUsername, username));

        if (update > 0) {
            redisUtil.del("Token_" + username);
            return ResultTool.success("修改密码成功，请登录");
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult setNewUserStatus(Boolean isNews) {
        User user = UserThreadLocal.get();
        int res = userMapper.update(user, new LambdaUpdateWrapper<User>()
                .eq(User::getUserId, user.getUserId())
                .set(User::getIsNews, isNews));
        if (res > 0) {
            return ResultTool.success("修改用户状态成功");
        } else {
            return ResultTool.fail();
        }
    }
}
