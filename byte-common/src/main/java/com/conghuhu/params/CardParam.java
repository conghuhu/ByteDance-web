package com.conghuhu.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardParam {
    private Long cardId;

    private String cardname;

    private String description;

    @TableField("listId")
    private Long listId;

    @TableField("productId")
    private Long productId;
    private Boolean closed;
    private Float pos;
    private LocalDateTime deadline;
    private Boolean tag;

    private Boolean executor;
    private LocalDateTime begintime;

    private Boolean expired;
}
