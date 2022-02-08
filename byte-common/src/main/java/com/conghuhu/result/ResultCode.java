package com.conghuhu.result;

/**
 * @Author: conghuhu
 * @Description: 返回码定义
 * 规定:
 * #1表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * @Date Create in 2021.9.30
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(500, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    PARAMS_ERROR(10001, "参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002, "用户名或密码不存在"),
    TOKEN_ERROR(10003, "token不合法"),
    ACCOUNT_EXIST(10004, "账号已存在"),
    SESSION_TIME_OUT(90001, "会话超时"),
    UPLOAD_FAIL(20001, "上传失败"),

    /* 用户错误 */
    USER_NOT_LOGIN(4010, "用户未登录"),
    USER_ACCOUNT_EXPIRED(4011, "账号已过期，请重新登录"),
    USER_CREDENTIALS_ERROR(4012, "密码错误"),
    USER_ACCOUNT_DISABLE(4013, "账号不可用"),
    USER_ACCOUNT_NOT_EXIST(4014, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(4015, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(4016, "账号下线"),
    INVITE_USER_PRODUCT_ERROR(4017, "邀请者用户Id或项目id有误，请检查"),

    /* 邮件服务 */
    MAIL_SEND_ERROR(6000, "邮件发送失败，请重试"),
    MAIL_MESSAGE_ERROR(6001, "邮件消息生成失败"),
    MAIL_PARAM_ERROR(6002, "邮件参数有误，请检查"),
    MAIL_HTML_ERROR(6003, "读取html文件错误"),
    MAIL_TEMPLATE_ERROR(6004, "freeMaker生成邮件模板错误"),
    MAIL_CODE_ALREADY_SEND(6005, "已发送验证码至邮箱，稍后重试"),
    MAIL_CODE_ERROR(6006, "验证码有误，请检查"),

    /* 查询错误 */
    NOT_FOUND(20010, "查询无果"),

    /* 标签错误 */
    TAG_CONSIST(21000, "标签已存在"),

    TAG_ID_NOT_CONSIST(21005, "tagId有误，请检查"),

    /* 卡片错误 */
    CARD_CONSIST(21000, "卡片已存在"),
    CARD_ID_NOT_CONSIST(21005, "cardId有误，请检查"),

    /* 项目错误 */
    PRODUCT_CONSIST(21000, "项目名称已存在，请更改"),
    PRODUCT_NOT_CONSIST(22000, "项目不存在，检查productId"),
    PRODUCT_PARAM_ERROR(23000, "项目参数有误，请检查"),
    PRODUCT_NOT_PERMISSION(70000, "用户没有该项目的权限"),

    /* 业务错误 */
    NO_PERMISSION(70001, "没有权限");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}

