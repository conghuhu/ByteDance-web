package com.conghuhu.controller;

import com.conghuhu.pubSub.PubClient;
import com.conghuhu.utils.JedisUtil;
import com.conghuhu.utils.LocalIPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author conghuhu
 * @create 2022-03-13 9:11
 */
@Slf4j
@RestController
@RequestMapping("/jedis")
public class JedisController {

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private PubClient pubClient;

    @Autowired
    private LocalIPUtil localIPUtil;

    @PostMapping("/pub")
    public void testPub() {
        pubClient.pub("product_message", "你好啊");
    }

    @PostMapping("/getIp")
    public String getIp(HttpServletRequest request) {
        String url = localIPUtil.getUrl();
        return url;
    }
}
