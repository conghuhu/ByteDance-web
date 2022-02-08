package com.conghuhu.controller;


import com.conghuhu.entity.Product;
import com.conghuhu.params.CreateProductParam;
import com.conghuhu.params.InviteParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.ProductService;
import com.conghuhu.vo.PersonProductVo;
import com.conghuhu.vo.ProductInitShowVo;
import com.conghuhu.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @ApiOperation(value = "创建新项目", notes = "创建新项目", produces = "application/json")
    @PostMapping("/create")
    public JsonResult<Product> createProduct(@RequestBody CreateProductParam productParam) {
        return productService.createProduct(productParam);
    }

    @ApiOperation(value = "删除项目", notes = "删除项目", produces = "application/json")
    @DeleteMapping("/delete")
    public JsonResult deleteProduct(@RequestParam(name = "id") Long id) {
        return productService.deleteProductById(id);
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

    @ApiOperation(value = "获取个人创建和参与的项目", notes = "获取个人创建和参与的项目", produces = "application/json")
    @GetMapping("/getPersonProduct")
    public JsonResult<PersonProductVo> getPersonProduct() {
        return productService.getPersonProduct();
    }


    @ApiOperation(value = "设置项目的背景", notes = "设置项目的背景", produces = "application/json")
    @PostMapping("/setProductBackground/{productId}")
    public JsonResult setProductBackground(@RequestParam("background") String background, @PathVariable Long productId) {
        return productService.setProductBackground(productId, background);
    }

    @ApiOperation(value = "他人受邀加入项目", notes = "他人受邀加入项目", produces = "application/json")
    @PostMapping("/invite")
    public JsonResult inviteUser(@RequestBody InviteParam inviteParam) {
        return productService.inviteUser(inviteParam);
    }

    @ApiOperation(value = "获取邀请展示信息(无需token)", notes = "获取邀请展示信息", produces = "application/json")
    @GetMapping("/getInviteInfo")
    public JsonResult getInviteInfo(
            @RequestParam("productId") Long productId,
            @RequestParam("secret") String secret,
            HttpServletRequest request
    ) {
        return productService.getInviteInfo(productId, secret,request);
    }


    @ApiOperation(value = "获取项目成员列表", notes = "获取项目成员列表", produces = "application/json")
    @GetMapping("/getMemberList/{productId}")
    public JsonResult<List<UserVo>> getMemberList(@PathVariable Long productId) {
        List<UserVo> memberList = productService.getMemberList(productId);
        if (memberList != null) {
            return ResultTool.success(memberList);
        } else {
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "判断成员在项目中的身份信息", notes = "判断成员在项目中的身份信息", produces = "application/json")
    @GetMapping("/getMemberStatus/{productId}")
    public JsonResult getMemberStatus(@PathVariable Long productId) {
        return productService.getMemberStatus(productId);
    }

}
