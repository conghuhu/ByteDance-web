package com.conghuhu.vo;


import com.conghuhu.entity.Tag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author conghuhu
 * @create 2022-01-29 15:17
 */
@Data
public class CardVo extends WebsocketDetail {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long cardId;

    private String cardname;

    private String description;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long listId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    private Boolean closed;

    private Float pos;

    private LocalDateTime deadline;

    private List<Tag> tagList;

    private List<UserVo> executorList;

    private LocalDateTime begintime;

    private Boolean expired;

    private LocalDateTime createdTime;

    private UserVo creator;

    private String background;

    private Boolean completed;

}
