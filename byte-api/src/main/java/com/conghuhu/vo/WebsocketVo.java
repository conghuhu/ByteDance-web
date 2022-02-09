package com.conghuhu.vo;

import lombok.Data;

import java.util.List;

/**
 * @author conghuhu
 * @create 2022-02-07 19:55
 */
@Data
public class WebsocketVo {

    private String typeName;

    private List<String> tags;

    private Long productId;

    private String event;

    private WebsocketDetail detail;

    private String identification;

}
