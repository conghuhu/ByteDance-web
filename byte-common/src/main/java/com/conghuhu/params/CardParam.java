package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardParam {

    private String cardname;

    private Long listId;

    private Long productId;

    private Float pos;

}
