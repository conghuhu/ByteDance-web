package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.Product;
import com.conghuhu.mapper.ProductMapper;
import com.conghuhu.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
