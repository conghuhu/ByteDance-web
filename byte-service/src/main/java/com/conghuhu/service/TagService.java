package com.conghuhu.service;

import com.conghuhu.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.params.TagParam;
import com.conghuhu.result.JsonResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
public interface TagService extends IService<Tag> {

    /**
     * 创建新标签
     * @param tagParam
     * @return
     */
    JsonResult createTag(TagParam tagParam);

    /**
     * 更新标签
     * @param tagParam
     * @return
     */
    JsonResult updateTag(TagParam tagParam);

    /**
     * 根据项目id获取当前项目下所有的tags
     * @param productId
     * @return
     */
    JsonResult getTagsByProductId(Long productId);

    /**
     * 根据id删除对应的标签
     * @param id
     * @return
     */
    JsonResult removeTagById(Long id);
}
