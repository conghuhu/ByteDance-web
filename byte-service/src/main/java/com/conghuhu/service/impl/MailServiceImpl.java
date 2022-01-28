package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.Mail;
import com.conghuhu.mapper.MailMapper;
import com.conghuhu.params.MailParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Service
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Override
    public JsonResult sendMail(MailParam mailParam) {
        try {
            checkMail(mailParam);
            sendMimeMail(mailParam);
            return ResultTool.success();
        } catch (Exception e) {
            return ResultTool.fail();
        }
    }

    private void checkMail(MailParam mailParam) {
        if (StringUtils.isEmpty(mailParam.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailParam.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailParam.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }


    private void sendMimeMail(MailParam mailParam) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            messageHelper.setFrom(mailParam.getFrom());
            messageHelper.setTo(mailParam.getTo().split(","));
            messageHelper.setSubject(mailParam.getSubject());
            messageHelper.setText(mailParam.getText());

            mailSender.send(messageHelper.getMimeMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
