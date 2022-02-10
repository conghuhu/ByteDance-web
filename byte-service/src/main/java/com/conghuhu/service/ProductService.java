package com.conghuhu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.entity.Product;
import com.conghuhu.params.CreateProductParam;
import com.conghuhu.params.InviteParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.vo.PersonProductVo;
import com.conghuhu.vo.ProductInitShowVo;
import com.conghuhu.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
public interface ProductService extends IService<Product> {

    /**
     * 获取页面初始加载信息
     *
     * @param productId
     * @return
     */
    ProductInitShowVo getProductShowInfo(Long productId);

    /**
     * 创建项目
     *
     * @param productParam
     * @return
     */
    JsonResult<Product> createProduct(CreateProductParam productParam);

    /**
     * 获取某个用户创建的项目
     *
     * @param userId
     * @return
     */
    JsonResult<List<Product>> getProductByUserId(Long userId);

    /**
     * 设置项目的背景
     *
     * @param productId
     * @param background
     * @return
     */
    JsonResult setProductBackground(Long productId, String background);

    /**
     * 邀请用户
     *
     * @param inviteParam
     * @return
     */
    JsonResult inviteUser(InviteParam inviteParam);

    /**
     * 获取项目成员列表
     *
     * @param productId
     * @return
     */
    List<UserVo> getMemberList(Long productId);

    /**
     * 根据id删除项目
     *
     * @param id
     * @return
     */
    JsonResult deleteProductById(Long id);

    /**
     * 获取成员在项目中的身份信息
     *
     * @param productId
     * @return
     */
    JsonResult getMemberStatus(Long productId);

    /**
     * 获取邀请展示信息
     *
     * @param productId
     * @param secret
     * @return
     */
    JsonResult getInviteInfo(Long productId, String secret,HttpServletRequest request);

    /**
     * 获取个人创建的项目
     *
     * @return
     */
    JsonResult<PersonProductVo> getPersonProduct();

    JsonResult changeProductName(Long productId, String productName);

    JsonResult quitProduct(Long productId, Long userId);

    JsonResult kickOutMember(Long productId, Long userId);
}
