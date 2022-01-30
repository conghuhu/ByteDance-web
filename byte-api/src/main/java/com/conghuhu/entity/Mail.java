package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
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
 * @since 2022-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("byte_mail")
@ApiModel(value="Mail对象", description="")
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("mailId")
    private Long mailId;

    private String from;

    private String to;

    private String subject;

    private String text;

    private LocalDateTime sendtime;


}
