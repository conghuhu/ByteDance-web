package com.conghuhu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author conghuhu
 * @create 2022-02-07 20:22
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WebsocketDetail {
    private Long id;

    private String name;

    private Long productId;

    private Long listAfterId;

    private Long listBeforeId;

    private Float pos;

    private Boolean closed;

    private UserVo newMember;

    private String background;
}
