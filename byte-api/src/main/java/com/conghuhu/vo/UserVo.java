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

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String username;

    private String avatar;

    private String fullname;

    private Boolean isNews;

}
