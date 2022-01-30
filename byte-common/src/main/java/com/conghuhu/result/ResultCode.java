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
    COMMON_FAIL(999, "失败"),

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
    USER_NOT_LOGIN(90002, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    INVITE_USER_NOT_CONSIST(2010, "邀请者用户Id不存在，请检查"),

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

