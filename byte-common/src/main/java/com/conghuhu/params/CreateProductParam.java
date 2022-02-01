package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author conghuhu
 * @create 2022-01-28 19:08
 */
@Data
public class CreateProductParam {

    private Long ownerId;

    private Boolean isPrivate;

    private String productName;

    private String description;

    private String background;

}
