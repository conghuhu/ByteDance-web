package com.conghuhu.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.conghuhu.entity.User;
import com.conghuhu.mapper.UserMapper;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.RedisUtil;
import com.conghuhu.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author conghuhu
 * @create 2021-09-29 16:41
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final RedisUtil redisUtil;

    private final UserMapper userMapper;

    public MyUserDetailsService(UserMapper userMapper, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始身份验证,用户名为：" + username);

        User user = new User();
        User redisUser = (User) redisUtil.get("Token_" + username);
        User dbUser;
        if (redisUser != null) {
            log.info("redis中找到User，判断为已登录：" + redisUser);
            BeanUtils.copyProperties(redisUser, user);
        } else {
            dbUser = userMapper.getByUserName(username);
            log.info("数据库中找到User：" + dbUser);
            if (dbUser == null) {
                log.error("用户名不存在，登录失败");
                throw new UsernameNotFoundException("用户名不存在，登录失败");
            }
            BeanUtils.copyProperties(dbUser, user);
        }

        // 存到UserThreadLocal中，后续拦截器使用
        UserThreadLocal.put(user);

        UserDetail userDetail = new UserDetail();
        userDetail.setUser(user);
        userDetail.setUsername(user.getUsername());
        userDetail.setPassword(user.getPassword());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("TEST");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);
        userDetail.setAuthorities(authorities);
        return userDetail;
    }
}
