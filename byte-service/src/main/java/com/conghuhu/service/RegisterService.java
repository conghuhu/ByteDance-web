package com.conghuhu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.entity.User;
import com.conghuhu.params.RegisterParam;
import com.conghuhu.result.JsonResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional // 事务
/**
 * @author conghuhu
 * @create 2021-10-11 20:45
 */
public interface RegisterService extends IService<User> {
    /**
     * 注册用户
     * @param registerParam
     * @return
     */
    JsonResult register(RegisterParam registerParam);
}
