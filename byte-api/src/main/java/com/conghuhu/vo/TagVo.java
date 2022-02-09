package com.conghuhu.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-02-09 16:26
 */
@Data
public class TagVo extends WebsocketDetail{

    @JsonSerialize(using = ToStringSerializer.class)
    private Long tagId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    private String tagName;

    private String color;
}
