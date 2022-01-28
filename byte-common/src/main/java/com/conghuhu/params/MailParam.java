package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MailParam {
    private Long mailId;

    private String from;

    private String to;

    private String subject;

    private String text;

}
