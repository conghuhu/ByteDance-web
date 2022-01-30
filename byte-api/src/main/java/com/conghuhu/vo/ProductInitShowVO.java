package com.conghuhu.vo;

import com.conghuhu.entity.Card;

import com.conghuhu.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author conghuhu
 * @create 2022-01-28 16:54
 */
@Data
public class ProductInitShowVo {

    private List<CardVo> cardList;

    private List<Tag> tagList;

    private List<com.conghuhu.entity.List> lists;

    private String productName;

    private Boolean isPrivate;

    private LocalDateTime createdTime;

    private List<UserVo> memberList;

}
