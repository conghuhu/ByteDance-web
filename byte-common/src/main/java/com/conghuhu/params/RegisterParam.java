package com.conghuhu.params;

import lombok.Data;

/**
 * 注册参数
 * @author conghuhu
 * @create 2021-10-11 20:41
 */
@Data
public class RegisterParam {

    private String username;

    private String password;

    private String verifyCode;

    private String fullName;

    private String avatar;

}
