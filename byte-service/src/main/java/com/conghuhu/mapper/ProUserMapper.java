package com.conghuhu.mapper;

import com.conghuhu.entity.ProUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conghuhu.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-30
 */
@Repository
@Mapper
public interface ProUserMapper extends BaseMapper<ProUser> {
    /**
     * 获取项目的成员列表
     *
     * @param productId
     * @return
     */
    List<UserVo> getMemberList(Long productId);
}
