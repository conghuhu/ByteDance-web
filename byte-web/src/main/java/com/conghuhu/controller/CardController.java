package com.conghuhu.controller;


import com.conghuhu.entity.Card;
import com.conghuhu.params.CardParam;
import com.conghuhu.params.TagParam;
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
 *  前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Api(tags = "卡片类")
@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @ApiOperation(value = "根据卡片id获取卡片信息", notes = "根据卡片id获取卡片信息", produces = "application/json")
    @GetMapping("/querybyid/{cardId}")
    public JsonResult getCardById(@PathVariable Integer cardId){
        Card card =cardService.getById(cardId);
        if(card != null){
            return ResultTool.success(card);
        }else{
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

    @ApiOperation(value = "根据卡片名获取卡片信息", notes = "根据卡片名获取卡片信息", produces = "application/json")
    @GetMapping("/querybyname/{cardname}")
    public JsonResult getCardByName(@PathVariable String cardname){
        Card card =cardService.getByName(cardname);
        if(card != null){
            return ResultTool.success(card);
        }else{
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
    public JsonResult addcard(@RequestBody CardParam cardParam){
        Card card =new Card();
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
        card.setExpire(false);
        if(cardService.addCard(card)=="success"){
            return ResultTool.success();
        }else{
            return ResultTool.fail();
        }
    }

    @ApiOperation(value = "根据id删除对应的卡片", notes = "根据id删除对应的卡片", produces = "application/json")
    @PostMapping("/removeCardById/{cardId}")
    public JsonResult removeCardById(@PathVariable Long cardId) {
        return cardService.removeCardById(cardId);
    }

    @ApiOperation(value = "获取当前列的卡片", notes = "获取当前列的卡片", produces = "application/json")
    @GetMapping("/getCardsByListId/{listId}")
    public JsonResult getCardsByListId(@RequestParam("listId") Long listId) {
        return cardService.getCardsByListId(listId);
    }
}
