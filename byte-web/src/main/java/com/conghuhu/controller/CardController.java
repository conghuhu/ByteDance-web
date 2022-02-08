package com.conghuhu.controller;


import com.conghuhu.entity.Card;
import com.conghuhu.params.*;

import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;

import com.conghuhu.vo.CardVo;
import com.conghuhu.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/queryById/{cardId}")
    public JsonResult<CardVo> getCardById(@PathVariable Long cardId) {
        return cardService.getCardById(cardId);
    }

    @GetMapping("/queryByName/{cardname}")
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
    public JsonResult<Card> createCard(@RequestBody CardParam CardParam) {
        return cardService.createTag(CardParam);
    }

    @PostMapping("/modify")
    public JsonResult updateCard(@RequestBody Card CardParam) {
        return cardService.updateTag(CardParam);
    }


    @ApiOperation(value = "根据id删除对应的卡片", notes = "根据id删除对应的卡片", produces = "application/json")
    @DeleteMapping("/removeCardById/{cardId}")
    public JsonResult removeCardById(@PathVariable Long cardId) {
        return cardService.removeCardById(cardId);
    }

    @ApiOperation(value = "获取当前列的卡片", notes = "获取当前列的卡片", produces = "application/json")
    @GetMapping("/getCardsByListId/{listId}")
    public JsonResult<List<Card>> getCardsByListId(@PathVariable("listId") Long listId) {
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

    @ApiOperation(value = "设置卡片的执行时间", notes = "设置卡片的执行时间,\n 当开始时间和结束时间都穿空字符串时，后台将取消已设置的时间\n 二者传时间戳时，为设置模式", produces = "application/json")
    @PostMapping("/setCardDeadline/{cardId}")
    public JsonResult setCardDeadline(@RequestBody CardDateParam cardDateParam, @PathVariable Long cardId) {
        return cardService.setCardDeadline(cardDateParam, cardId);
    }

    @ApiOperation(value = "移动卡片", notes = "移动卡片", produces = "application/json")
    @PostMapping("/moveCard")
    public JsonResult moveCard(@RequestBody CardMoveParam cardMoveParam) {
        return cardService.moveCard(cardMoveParam);
    }

    @ApiOperation(value = "设置卡片的执行人", notes = "设置卡片的执行人", produces = "application/json")
    @PostMapping("/setExecutor/{cardId}")
    public JsonResult<List<UserVo>> setExecutor(@RequestParam("userId") Long userId, @PathVariable Long cardId) {
        return cardService.setExecutor(userId, cardId);
    }

    @ApiOperation(value = "删除卡片的执行人", notes = "删除卡片的执行人", produces = "application/json")
    @DeleteMapping("/removeExecutor/{cardId}")
    public JsonResult removeExecutor(@RequestParam("userId") Long userId, @PathVariable Long cardId) {
        return cardService.removeExecutor(userId, cardId);
    }

    @ApiOperation(value = "设置卡片的背景色", notes = "设置卡片的背景色，不需要传#，只传后面的HEX值,如ffffff", produces = "application/json")
    @PostMapping("/setCardBackground/{cardId}")
    public JsonResult setCardBackground(@RequestParam("background") String background, @PathVariable Long cardId) {
        return cardService.setCardBackground(background, cardId);
    }

    @ApiOperation(value = "设置卡片的完成状态", notes = "设置卡片的完成状态，结合日期使用，completed为true则完成，false为未完成", produces = "application/json")
    @PostMapping("/changeCardCompleteStatus/{cardId}")
    public JsonResult changeCardCompleteStatus(@RequestParam("completed") Boolean completed, @PathVariable Long cardId) {
        return cardService.changeCardCompleteStatus(completed, cardId);
    }
}
