package com.conghuhu.controller;


import com.conghuhu.entity.Product;
import com.conghuhu.params.CreateProductParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ProductService;
import com.conghuhu.vo.ProductInitShowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Api(tags = "项目类")
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "创建新任务", notes = "创建新任务", produces = "application/json")
    @PostMapping("/create")
    public JsonResult<Product> createProduct(@RequestBody CreateProductParam productParam) {
        return productService.createProduct(productParam);
    }

    @ApiOperation(value = "删除任务", notes = "删除任务", produces = "application/json")
    @DeleteMapping("/delete")
    public JsonResult deleteProduct(@RequestParam(name = "id") String id) {
        boolean save = productService.removeById(id);
        if (save) {
            return ResultTool.success(ResultCode.SUCCESS);
        } else {
            return ResultTool.fail(ResultCode.COMMON_FAIL);
        }
    }

    @ApiOperation(value = "获取项目页面初始渲染数据", notes = "获取项目页面初始渲染数据", produces = "application/json")
    @GetMapping("/getProductShowInfo/{productId}")
    public JsonResult<ProductInitShowVo> getProductShowInfo(@PathVariable Long productId) {
        ProductInitShowVo productInitShowVO = productService.getProductShowInfo(productId);
        if (productInitShowVO != null) {
            return ResultTool.success(productInitShowVO);
        } else {
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "获取某人创建的项目", notes = "获取某人创建的项目", produces = "application/json")
    @GetMapping("/getProductByUserId/{userId}")
    public JsonResult<List<Product>> getProductByUserId(@PathVariable Long userId) {
        return productService.getProductByUserId(userId);
    }
}
