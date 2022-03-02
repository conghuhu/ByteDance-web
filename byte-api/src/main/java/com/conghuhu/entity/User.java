package com.conghuhu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private String mobilePhoneNumber;

    private String password;

    private Integer status;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private Boolean admin;

    private String salt;


}
