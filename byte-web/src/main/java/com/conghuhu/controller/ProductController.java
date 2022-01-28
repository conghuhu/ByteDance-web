package com.conghuhu.controller;


import com.conghuhu.entity.Product;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "创建新任务", notes = "创建新任务", produces = "application/json")
    @RequestMapping("/create")
    public JsonResult createProduct(@RequestBody Product product) {
        boolean save = productService.save(product);
        if (save) {
            return ResultTool.success(product);
        } else {
            return ResultTool.fail(ResultCode.COMMON_FAIL);
        }
    }

    @ApiOperation(value = "删除任务", notes = "删除任务", produces = "application/json")
    @DeleteMapping("/delete")
    public JsonResult deleteProduct(@RequestParam(name = "id",required = true) String id) {
        boolean save = productService.removeById(id);
        if (save) {
            return ResultTool.success(ResultCode.SUCCESS);
        } else {
            return ResultTool.fail(ResultCode.COMMON_FAIL);
        }
    }
}
