package com.conghuhu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.*;
import com.conghuhu.mapper.*;
import com.conghuhu.params.CreateProductParam;
import com.conghuhu.params.InviteParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
import com.conghuhu.service.ProductService;
import com.conghuhu.service.ThreadService;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.JwtTokenUtil;
import com.conghuhu.utils.UserThreadLocal;
import com.conghuhu.vo.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private final ThreadService threadService;

    private final ProUserMapper proUserMapper;

    private final CardService cardService;

    private final ListMapper listMapper;

    private final CardMapper cardMapper;

    private final TagMapper tagMapper;

    private final ProductMapper productMapper;

    private final UserService userService;

    private final UserMapper userMapper;

    private final CardUserMapper cardUserMapper;

    public ProductServiceImpl(TagMapper tagMapper, ListMapper listMapper,
                              CardMapper cardMapper, ProductMapper productMapper,
                              CardService cardService, ProUserMapper proUserMapper,
                              UserService userService, UserMapper userMapper, ThreadService threadService, CardUserMapper cardUserMapper) {
        this.tagMapper = tagMapper;
        this.listMapper = listMapper;
        this.cardMapper = cardMapper;
        this.productMapper = productMapper;
        this.cardService = cardService;
        this.proUserMapper = proUserMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.threadService = threadService;
        this.cardUserMapper = cardUserMapper;
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

        List<Card> cardList = cardMapper.selectList(new LambdaQueryWrapper<Card>()
                .eq(Card::getProductId, productId)
                .eq(Card::getClosed, false)
        );

        List<com.conghuhu.entity.List> lists = listMapper.selectList(new LambdaQueryWrapper<com.conghuhu.entity.List>()
                .eq(com.conghuhu.entity.List::getProductId, productId)
                .orderByAsc(com.conghuhu.entity.List::getPos)
        );

        List<CardVo> cardVoList = cardList.parallelStream().map(item -> {
            CardVo cardVo = new CardVo();
            BeanUtils.copyProperties(item, cardVo);
            List<Tag> tags = null;
            List<UserVo> executorList = null;
            Long cardId = item.getCardId();
            if (item.getTag()) {
                tags = cardMapper.getTagsByCardId(cardId);
            }
            if (item.getExecutor()) {
                executorList = cardService.getExecutorsByCardId(cardId);
            }
            UserVo creator = userService.findUserVoById(item.getCreator());
            cardVo.setTagList(tags != null ? tags : new ArrayList<>());
            cardVo.setExecutorList(executorList != null ? executorList : new ArrayList<>());
            cardVo.setCreator(creator);
            return cardVo;
        }).collect(Collectors.toList());

        List<UserVo> memberList = getMemberList(productId);
        showVO.setProductName(product.getProductName());
        showVO.setCardList(cardVoList);
        showVO.setTagList(tagList);
        showVO.setLists(lists);
        showVO.setIsPrivate(product.getIsPrivate());
        showVO.setCreatedTime(product.getCreatedTime());
        showVO.setMemberList(memberList);
        showVO.setBackground(product.getBackground());
        return showVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<Product> createProduct(CreateProductParam productParam) {
        Product product = new Product();
        Integer selectCount = productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductName, productParam.getProductName()));
        if (selectCount > 0) {
            return ResultTool.fail(ResultCode.PRODUCT_CONSIST);
        }
        BeanUtils.copyProperties(productParam, product);
        product.setCreatedTime(LocalDateTime.now());
        int res = productMapper.insert(product);

        ProUser proUser = new ProUser();
        proUser.setProductId(product.getId());
        proUser.setUserId(product.getOwnerId());
        proUser.setCreatedTime(LocalDateTime.now());
        int insert = proUserMapper.insert(proUser);
        if (res > 0 && insert > 0) {
            return ResultTool.success(product);
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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

    @Override
    public JsonResult setProductBackground(Long productId, String background) {
        if (!StringUtils.hasText(background) || productId == null) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }

        Product product = new Product();
        product.setId(productId);
        product.setBackground("#" + background);
        int res = productMapper.updateById(product);
        if (res > 0) {
            WebsocketDetail detail = WebsocketDetail.builder()
                    .productId(productId)
                    .background("#" + background)
                    .build();
            threadService.notifyAllMemberByProductId(productId,
                    "updateModels", "Product",
                    new ArrayList<>(Arrays.asList("updates", "background"))
                    , detail);
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult inviteUser(InviteParam inviteParam) {
        ProUser proUser = new ProUser();
        String secret = inviteParam.getSecret();
        Long productId = inviteParam.getProductId();
        Long userId = inviteParam.getUserId();
        if (productId == null || userId == null || !StringUtils.hasText(secret)) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }
        if (userId == 0 || productId == 0) {
            return ResultTool.fail(ResultCode.PARAMS_ERROR);
        }
        Long inviteUserId = Long.valueOf(userService.getUserIdByInviteCode(secret, "cong0917"));
        // 自己邀请自己，直接进项目
        if (inviteUserId.equals(userId)) {
            return ResultTool.success();
        }
        Integer selectInviteUserCount = productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getId, productId)
                .eq(Product::getOwnerId, inviteUserId));
        // 必须项目的 Owner 才有资格邀请他人
        if (selectInviteUserCount <= 0) {
            return ResultTool.fail(ResultCode.INVITE_USER_PRODUCT_ERROR);
        }
        proUser.setUserId(userId);
        proUser.setProductId(productId);
        proUser.setInviteUserId(inviteUserId);
        Integer isConsist = proUserMapper.selectCount(new LambdaQueryWrapper<ProUser>()
                .eq(ProUser::getUserId, userId)
                .eq(ProUser::getProductId, productId)
                .eq(ProUser::getInviteUserId, inviteUserId)
        );
        if (isConsist > 0) {
            return ResultTool.success();
        }
        proUser.setCreatedTime(LocalDateTime.now());
        int res = proUserMapper.insert(proUser);
        if (res > 0) {
            User user = userMapper.selectById(userId);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            WebsocketDetail detail = WebsocketDetail.builder()
                    .productId(productId)
                    .newMember(userVo)
                    .build();
            threadService.notifyAllMemberByProductId(productId,
                    "updateModels", "Product",
                    new ArrayList<>(Arrays.asList("updates", "member"))
                    , detail);
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public List<UserVo> getMemberList(Long productId) {
        List<UserVo> memberList = proUserMapper.getMemberList(productId);
        return memberList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult deleteProductById(Long id) {
        int delete = productMapper.deleteById(id);
        proUserMapper.delete(new LambdaQueryWrapper<ProUser>().eq(ProUser::getProductId, id));
        // 还应删除对应的card以及相关的关联
        if (delete > 0) {
            return ResultTool.success();
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult getMemberStatus(Long productId) {
        User user = UserThreadLocal.get();
        Integer count = productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getId, productId)
                .eq(Product::getOwnerId, user.getUserId()));
        JSONObject jsonObject = new JSONObject();
        if (count > 0) {
            jsonObject.put("isOwner", true);
        } else {
            jsonObject.put("isOwner", false);
        }
        return ResultTool.success(jsonObject);
    }

    @Override
    public JsonResult getInviteInfo(Long productId, String secret, HttpServletRequest request) {
        // 获取Token字符串，token 置于 header 里
        String token = request.getHeader("token");
        boolean tokenExpired = true;
        if (token != null && !"".equals(token.trim())) {
            try {
                JwtTokenUtil.getUserNameFromToken(token);
                tokenExpired = false;
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                tokenExpired = true;
            } catch (Exception e) {
                e.printStackTrace();
                tokenExpired = true;
            }
        }
        Long inviteUserId = Long.valueOf(userService.getUserIdByInviteCode(secret, "cong0917"));
        String productName = productMapper.getProductNameById(productId);
        String inviteUserName = userMapper.getUserNameById(inviteUserId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productName", productName);
        jsonObject.put("inviteUserName", inviteUserName);
        jsonObject.put("tokenExpire", tokenExpired);
        return ResultTool.success(jsonObject);
    }

    @Override
    public JsonResult<PersonProductVo> getPersonProduct() {
        User user = UserThreadLocal.get();
        Long userId = user.getUserId();
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getOwnerId, userId)
                .orderByDesc(Product::getCreatedTime));
        List<Product> shareProductList = productMapper.getShareProductByUserId(userId);
        PersonProductVo personProductVo = new PersonProductVo();
        if (productList != null && shareProductList != null) {
            personProductVo.setProductList(productList);
            personProductVo.setShareProductList(shareProductList);
            return ResultTool.success(personProductVo);
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult changeProductName(Long productId, String productName) {
        Product product = new Product();
        product.setId(productId);
        product.setProductName(productName);
        int res = productMapper.updateById(product);
        if (res > 0) {
            WebsocketDetail detail = WebsocketDetail.builder()
                    .name(productName)
                    .build();
            threadService.notifyAllMemberByProductId(productId,
                    "updateModels", "Product",
                    new ArrayList<>(Arrays.asList("updates", "name"))
                    , detail);
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult quitProduct(Long productId, Long userId) {
        return threadDeleteCardUser(productId, userId);
    }

    @Override
    public JsonResult kickOutMember(Long productId, Long userId) {
        User user = UserThreadLocal.get();
        Product product = productMapper.selectById(productId);
        if (!product.getOwnerId().equals(user.getUserId())) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
        }

        return threadDeleteCardUser(productId, userId);
    }

    @NotNull
    private JsonResult threadDeleteCardUser(Long productId, Long userId) {
        int delete = proUserMapper.delete(new LambdaQueryWrapper<ProUser>()
                .eq(ProUser::getProductId, productId)
                .eq(ProUser::getUserId, userId));
        if (delete > 0) {
            threadService.deleteCardUserByProductId(productId, userId);
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }
}
