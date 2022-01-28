package com.conghuhu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.entity.Mail;
import com.conghuhu.params.MailParam;
import com.conghuhu.result.JsonResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
public interface MailService extends IService<Mail> {

    JsonResult sendMail(MailParam mailParam);

}
