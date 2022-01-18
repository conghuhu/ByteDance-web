package com.conghuhu.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2021-10-13 9:09
 */
@Data
public class UserVo {

    private String username;

    private String avatar;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
