package com.conghuhu.params;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kongdong
 */
@Data
public class MailParam {

    private Long mailId;

    private String from;

    private String to;

    private String subject;

    private String text;

}
