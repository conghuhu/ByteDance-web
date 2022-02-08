package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@ApiModel(value = "Card对象", description = "")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "cardId", type = IdType.AUTO)
    private Long cardId;

    private String cardname;

    private String description;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("listId")
    private Long listId;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("productId")
    private Long productId;

    private Boolean closed;

    private Float pos;

    private LocalDateTime deadline;

    private Boolean tag;

    private Boolean executor;

    private LocalDateTime begintime;

    private Boolean expired;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long creator;

    private String background;

    private Boolean completed;
}
