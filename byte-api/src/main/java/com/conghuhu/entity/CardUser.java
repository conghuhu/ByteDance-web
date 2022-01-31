package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("byte_card_user")
@ApiModel(value = "CardUser对象", description = "")
public class CardUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "卡片id")
    @TableField("cardId")
    private Long cardId;

    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private Long userId;

}
