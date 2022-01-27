package com.conghuhu.controller;


import com.conghuhu.entity.Card;
import com.conghuhu.params.CardParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
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
@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping("/querybyid/{cardId}")
    public JsonResult getCardById(@PathVariable Integer cardId){
        Card card =cardService.getById(cardId);
        if(card != null){
            return ResultTool.success(card);
        }else{
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }


    @GetMapping("/querybyname/{cardname}")
    public JsonResult getCardByName(@PathVariable String cardname){
        Card card =cardService.getByName(cardname);
        if(card != null){
            return ResultTool.success(card);
        }else{
            return ResultTool.fail(ResultCode.NOT_FOUND);
        }
    }

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
}
