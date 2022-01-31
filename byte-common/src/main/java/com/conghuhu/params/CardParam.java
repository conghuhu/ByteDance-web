package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardParam {

    private String cardname;

    private String description;

    private Long listId;

    private Long productId;

    private Float pos;

    private LocalDateTime deadline;

    private LocalDateTime begintime;
}
