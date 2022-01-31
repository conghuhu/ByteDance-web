package com.conghuhu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.User;
import com.conghuhu.mapper.UserMapper;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.AESUtil;
import com.conghuhu.utils.HexConversion;
import com.conghuhu.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    private final StringRedisTemplate stringRedisTemplate;

    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
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
        String userJson = stringRedisTemplate.opsForValue().get("Token_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        } else {
            User user = JSON.parseObject(userJson, User.class);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user,userVo);
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
}
