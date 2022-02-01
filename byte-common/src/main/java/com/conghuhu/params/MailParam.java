package com.conghuhu.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author kongdong
 */
@Data
@ApiModel(value = "Card对象", description = "")
public class MailParam {

    private Long mailId;

    @ApiModelProperty(value = "邮件的发送对象，多个邮件用`，`相连")
    private String to;

    @ApiModelProperty(value = "邮件的主题")
    private String subject;

    @ApiModelProperty(value = "邮件html内容的参数map")
    Map<String, Object> model;

    @ApiModelProperty(value = "模板文件的名称")
    private String templateFile;

}
