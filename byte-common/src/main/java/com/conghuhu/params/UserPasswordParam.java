package com.conghuhu.params;

import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-02-01 13:33
 */
@Data
public class UserPasswordParam {

    private String username;

    private String password;

    private String verifyCode;

}
