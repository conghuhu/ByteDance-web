package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conghuhu.entity.List;
import com.conghuhu.entity.Product;
import com.conghuhu.mapper.ListMapper;
import com.conghuhu.mapper.ProductMapper;
import com.conghuhu.params.ListMoveParam;
import com.conghuhu.params.ListParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Service
public class ListServiceImpl extends ServiceImpl<ListMapper, List> implements ListService {

    private final ProductMapper productMapper;

    private final ListMapper listMapper;

    public ListServiceImpl(ListMapper listMapper, ProductMapper productMapper) {
        this.listMapper = listMapper;
        this.productMapper = productMapper;
    }

    @Override
    public JsonResult addList(ListParam listParam) {
        Long productId = listParam.getProductId();
        Float pos = listParam.getPos();
        String listName = listParam.getListName();
        String backgroundColor = listParam.getBackgroundColor();

        Integer productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getId, productId));

        if (productCount <= 0) {
            return ResultTool.fail(ResultCode.PRODUCT_NOT_CONSIST);
        }

        List list = new List();
        list.setProductId(productId);
        list.setPos(pos);
        list.setListName(listName);
        list.setBackgroundColor(backgroundColor);
        list.setCreatedTime(LocalDateTime.now());
        int res = listMapper.insert(list);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult closeList(Long listId) {
        List list = new List();
        list.setId(listId);
        list.setClosed(true);
        int res = listMapper.updateById(list);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult editListName(Long listId, String listName) {
        List list = new List();
        list.setId(listId);
        list.setListName(listName);
        int res = listMapper.updateById(list);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult editBackGroundColor(Long listId, String backgroundColor) {
        List list = new List();
        list.setId(listId);
        list.setBackgroundColor(backgroundColor);
        int res = listMapper.updateById(list);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult moveList(ListMoveParam listMoveParam) {
        Float pos = listMoveParam.getPos();
        Long listId = listMoveParam.getListId();
        List list = new List();
        list.setId(listId);
        list.setPos(pos);

        int res = listMapper.updateById(list);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult removeList(Long listId) {
        int delete = listMapper.deleteById(listId);
        // 删除列后还应将其中的card删除，包含card关联的数据（待开发）
        if (delete > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }
}
