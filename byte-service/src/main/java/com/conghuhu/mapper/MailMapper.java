package com.conghuhu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conghuhu.entity.Mail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Repository
@Mapper
public interface MailMapper extends BaseMapper<Mail> {

}
