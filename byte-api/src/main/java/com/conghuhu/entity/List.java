package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("byte_list")
@ApiModel(value="List对象", description="")
public class List implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目Id")
    @TableField("productId")
    private Long productId;

    @ApiModelProperty(value = "位置权重")
    private Float pos;

    @ApiModelProperty(value = "0 未关闭")
    private Boolean closed;

    @ApiModelProperty(value = "列的名称")
    @TableField("listName")
    private String listName;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;


}
