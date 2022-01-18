package com.conghuhu.params;

import lombok.Data;

/**
 * @author conghuhu
 * @create 2021-10-11 20:41
 */
@Data
public class RegisterParam {

    private String username;

    private String password;

    private String email;

    private String mobilePhoneNumber;
}
