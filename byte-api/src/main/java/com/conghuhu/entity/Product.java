package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("byte_product")
@ApiModel(value="Product对象", description="")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("ownerId")
    private Long ownerId;

    @ApiModelProperty(value = "0 他人不可见")
    @TableField("isPrivate")
    private Boolean isPrivate;

    @TableField("productName")
    private String productName;

    private String description;

    private String background;

    private LocalDateTime createdTime;


}
