package com.conghuhu.mapper;

import com.conghuhu.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Repository
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 通过项目Id获取项目名称
     *
     * @param productId
     * @return
     */
    String getProductNameById(Long productId);

    /**
     * 获取某人协作参与的项目
     *
     * @param userId
     * @return
     */
    List<Product> getShareProductByUserId(Long userId);
}
