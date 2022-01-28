package com.conghuhu.controller;


import com.conghuhu.entity.Product;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/create")
    public JsonResult<Product> createProduct(@RequestBody Product product) {
        boolean save = productService.save(product);
        if (save) {
            return ResultTool.success(product);
        } else {
            return ResultTool.fail(ResultCode.COMMON_FAIL);
        }
    }
}
