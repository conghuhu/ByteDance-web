package com.conghuhu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conghuhu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2021-09-25
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getByUserName(@Param("username") String name);

    String getUserNameById(Long userId);
}
