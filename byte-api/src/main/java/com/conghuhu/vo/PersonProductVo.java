package com.conghuhu.vo;

import com.conghuhu.entity.Product;
import lombok.Data;

import java.util.List;

/**
 * @author conghuhu
 * @create 2022-01-31 15:41
 */
@Data
public class PersonProductVo {

    private List<Product> productList;

    private List<Product> shareProductList;

}
