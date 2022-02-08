package com.conghuhu.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-02-07 20:22
 */
@Builder
@Data
public class WebsocketDetail {
    private Long id;

    private String name;

    private Long productId;

    private Long listAfterId;

    private Float pos;

    private Boolean closed;

    private Long listBeforeId;
}
