package com.conghuhu.params;

import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-01-30 10:47
 */
@Data
public class InviteParam {

    private Long productId;

    /**
     * 邀请码
     */
    private String secret;

    private Long userId;

}
