package com.conghuhu.params;

import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-01-27 13:39
 */
@Data
public class TagParam {

    private Long id;

    private String tagName;

    private String color;

    private Long productId;
}
