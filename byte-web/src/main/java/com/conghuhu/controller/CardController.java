package com.conghuhu.controller;


import com.conghuhu.entity.Card;
import com.conghuhu.params.CardDateParam;
import com.conghuhu.params.CardParam;
import com.conghuhu.params.EditParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
import com.qiniu.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Api(tags = "卡片类")
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @ApiOperation(value = "通过cardId查找卡片", notes = "通过cardId查找卡片", produces = "application/json")
    @GetMapping("/queryById/{cardId}")
    public JsonResult getCardById(@PathVariable Integer cardId) {
        Card card = cardService.getById(cardId);
        if (card != null) {
            return ResultTool.success(card);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "通过cardname查找卡片", notes = "通过cardname查找卡片", produces = "application/json")
    @GetMapping("/queryByName/{cardname}")
    public JsonResult getCardByName(@PathVariable String cardname) {
        Card card = cardService.getByName(cardname);
        if (card != null) {
            return ResultTool.success(card);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "新建卡片", notes = "新建卡片", produces = "application/json")
    @PostMapping("/addCard")
    public JsonResult addCard(@RequestBody CardParam cardParam) {
        Card card = new Card();
        card.setCardname(cardParam.getCardname());
        card.setDescription(cardParam.getDescription());
        card.setListId(cardParam.getListId());
        card.setProductId(cardParam.getProductId());
        card.setClosed(false);
        card.setPos(cardParam.getPos());
        card.setDeadline(LocalDateTime.now());
        card.setTag(cardParam.getTag());
        card.setExecutor(cardParam.getExecutor());
        card.setBegintime(LocalDateTime.now());
        card.setExpired(false);
        if (cardService.addCard(card) == "success") {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "编辑卡片的desc", notes = "编辑卡片的desc", produces = "application/json")
    @PostMapping("/editDescByCardId/{cardId}")
    public JsonResult editDescByCardId(@RequestBody EditParam editParam, @PathVariable Long cardId) {
        return cardService.editDescByCardId(editParam, cardId);
    }

    @ApiOperation(value = "编辑卡片的标题", notes = "编辑卡片的标题", produces = "application/json")
    @PostMapping("/editCardNameByCardId/{cardId}")
    public JsonResult editCardNameByCardId(@RequestBody EditParam editParam, @PathVariable Long cardId) {
        return cardService.editCardNameByCardId(editParam, cardId);
    }

    @ApiOperation(value = "设置卡片的执行时间", notes = "设置卡片的执行时间", produces = "application/json")
    @PostMapping("/setCardDeadline/{cardId}")
    public JsonResult setCardDeadline(@RequestBody CardDateParam cardDateParam, @PathVariable Long cardId) {
        return cardService.setCardDeadline(cardDateParam, cardId);
    }
}
