package com.conghuhu.service;

import com.conghuhu.entity.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.params.ListMoveParam;
import com.conghuhu.params.ListParam;
import com.conghuhu.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
public interface ListService extends IService<List> {

    /**
     * 增加列
     *
     * @param listParam
     * @return
     */
    JsonResult addList(ListParam listParam);

    /**
     * 关闭（归档）列
     *
     * @param listId
     * @return
     */
    JsonResult closeList(Long listId);

    /**
     * 编辑列的名称
     *
     * @param listId
     * @param listName
     * @return
     */
    JsonResult editListName(Long listId, String listName);

    /**
     * 编辑列的背景颜色
     * @param listId
     * @param backgroundColor
     * @return
     */
    JsonResult editBackGroundColor(Long listId, String backgroundColor);

    /**
     * 移动列
     * @param listMoveParam
     * @return
     */
    JsonResult moveList(ListMoveParam listMoveParam);

    /**
     * 删除列
     * @param listId
     * @return
     */
    JsonResult removeList(Long listId);
}
