package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author conghuhu
 * @create 2022-01-27 22:03
 */
@Data
public class CardDateParam {

    private LocalDateTime beginTime;

    private LocalDateTime deadline;

    private Long dueReminder;
}
