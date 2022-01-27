package com.conghuhu.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardParam {
    private String cardname;

    private String description;

    @TableField("listId")
    private Long listId;

    @TableField("productId")
    private Long productId;

    private Float pos;

    private Boolean tag;

    private Boolean executor;
}
