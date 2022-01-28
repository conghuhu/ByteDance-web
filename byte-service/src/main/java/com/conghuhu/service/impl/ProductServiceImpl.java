package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.Card;
import com.conghuhu.entity.Product;
import com.conghuhu.entity.Tag;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.mapper.ListMapper;
import com.conghuhu.mapper.ProductMapper;
import com.conghuhu.mapper.TagMapper;
import com.conghuhu.params.CreateProductParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ProductService;
import com.conghuhu.vo.ProductInitShowVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ListMapper listMapper;

    private final CardMapper cardMapper;

    private final TagMapper tagMapper;

    private final ProductMapper productMapper;

    public ProductServiceImpl(TagMapper tagMapper, ListMapper listMapper, CardMapper cardMapper, ProductMapper productMapper) {
        this.tagMapper = tagMapper;
        this.listMapper = listMapper;
        this.cardMapper = cardMapper;
        this.productMapper = productMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductInitShowVo getProductShowInfo(Long productId) {
        ProductInitShowVo showVO = new ProductInitShowVo();
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return showVO;
        }

        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>().eq(Tag::getProductId, productId));

        List<Card> cardList = cardMapper.selectList(new LambdaQueryWrapper<Card>().eq(Card::getProductId, productId));

        List<com.conghuhu.entity.List> lists = listMapper.selectList(new LambdaQueryWrapper<com.conghuhu.entity.List>().eq(com.conghuhu.entity.List::getProductId, productId));

        showVO.setProductName(product.getProductName());
        showVO.setCardList(cardList);
        showVO.setTagList(tagList);
        showVO.setLists(lists);
        showVO.setIsPrivate(product.getIsPrivate());
        showVO.setCreatedTime(product.getCreatedTime());
        return showVO;
    }

    @Override
    public JsonResult<Product> createProduct(CreateProductParam productParam) {
        Product product = new Product();
        Integer selectCount = productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getProductName, productParam.getProductName()));
        if (selectCount > 0) {
            return ResultTool.fail(ResultCode.PRODUCT_CONSIST);
        }
        BeanUtils.copyProperties(productParam, product);
        int res = productMapper.insert(product);
        if (res > 0) {
            return ResultTool.success(product);
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult<List<Product>> getProductByUserId(Long userId) {

        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getOwnerId, userId));
        if (productList != null) {
            return ResultTool.success(productList);
        } else {
            return ResultTool.fail();
        }
    }
}
