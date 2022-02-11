package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.Mail;
import com.conghuhu.mapper.MailMapper;
import com.conghuhu.params.MailParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.MailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Service
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public JsonResult sendMail(MailParam mailParam) {
        try {
            checkMail(mailParam);
            sendMimeMail(mailParam);
            return ResultTool.success("邮件发送成功");
        } catch (MailException e) {
            e.printStackTrace();
            return ResultTool.fail(ResultCode.MAIL_SEND_ERROR);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResultTool.fail(ResultCode.MAIL_MESSAGE_ERROR);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultTool.fail(ResultCode.MAIL_PARAM_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultTool.fail(ResultCode.MAIL_HTML_ERROR);
        } catch (TemplateException e) {
            e.printStackTrace();
            return ResultTool.fail(ResultCode.MAIL_TEMPLATE_ERROR);
        }
    }

    private void checkMail(MailParam mailParam) {
        if (!StringUtils.hasLength(mailParam.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (!StringUtils.hasLength(mailParam.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
    }


    private void sendMimeMail(MailParam mailParam)
            throws MessagingException, MailException, IOException, TemplateException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(mailParam.getTo().split(","));
        messageHelper.setSubject(mailParam.getSubject());
        //邮件内容   true 表示带有附件或html
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(mailParam.getTemplateFile());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailParam.getModel());
        messageHelper.setText(html, true);
        mailSender.send(messageHelper.getMimeMessage());
    }



}
