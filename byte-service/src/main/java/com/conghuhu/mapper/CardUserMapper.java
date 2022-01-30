package com.conghuhu.mapper;

import com.conghuhu.entity.CardUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-29
 */
@Repository
@Mapper
public interface CardUserMapper extends BaseMapper<CardUser> {

}
