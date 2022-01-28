package com.conghuhu.controller;


import com.conghuhu.entity.Card;
import com.conghuhu.params.CardDateParam;
import com.conghuhu.params.CardMoveParam;
import com.conghuhu.params.CardParam;

import com.conghuhu.params.EditParam;

import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

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

    @ApiOperation(value = "根据卡片id获取卡片信息", notes = "根据卡片id获取卡片信息", produces = "application/json")
    @GetMapping("/querybyid/{cardId}")
    public JsonResult getCardById(@PathVariable Integer cardId) {
        Card card = cardService.getById(cardId);
        if (card != null) {
            return ResultTool.success(card);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "根据卡片名获取卡片信息", notes = "根据卡片名获取卡片信息", produces = "application/json")
    @GetMapping("/querybyname/{cardname}")
    public JsonResult getCardByName(@PathVariable String cardname) {
        Card card = cardService.getByName(cardname);
        if (card != null) {
            return ResultTool.success(card);
        } else {
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "创建新卡片(卡片名字若存在创建失败)", notes = "创建新卡片(卡片名字若存在创建失败)", produces = "application/json")
    @PostMapping("/create")
    public JsonResult createCard(@RequestBody CardParam CardParam) {
        return cardService.createTag(CardParam);
    }

    @ApiOperation(value = "修改卡片", notes = "修改卡片", produces = "application/json")
    @PostMapping("/modify")
    public JsonResult updateCard(@RequestBody CardParam CardParam) {
        return cardService.updateTag(CardParam);
    }

    @ApiOperation(value = "创建新卡片", notes = "创建新卡片", produces = "application/json")
    @PostMapping("/addcard")
    public JsonResult addcard(@RequestBody CardParam cardParam) {
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

    @ApiOperation(value = "根据id删除对应的卡片", notes = "根据id删除对应的卡片", produces = "application/json")
    @DeleteMapping("/removeCardById/{cardId}")
    public JsonResult removeCardById(@PathVariable Long cardId) {
        return cardService.removeCardById(cardId);
    }

    @ApiOperation(value = "获取当前列的卡片", notes = "获取当前列的卡片", produces = "application/json")
    @GetMapping("/getCardsByListId/{listId}")
    public JsonResult getCardsByListId(@PathVariable Long listId) {
        return cardService.getCardsByListId(listId);
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

    @ApiOperation(value = "移动卡片", notes = "移动卡片", produces = "application/json")
    @PostMapping("/moveCard")
    public JsonResult moveCard(@RequestBody CardMoveParam cardMoveParam){
        return cardService.moveCard(cardMoveParam);
    }
}
