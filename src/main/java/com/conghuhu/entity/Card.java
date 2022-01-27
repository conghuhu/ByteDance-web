package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("byte_card")
@ApiModel(value="Card对象", description="")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("cardId")
    private Long cardId;

    private String name;

    private String desc;

    @TableField("listId")
    private Long listId;

    @TableField("productId")
    private Long productId;

    private Boolean closed;

    private Float pos;

    private LocalDateTime deadline;

    private Boolean tag;

    private Boolean executor;


}
