package com.david.notificacao.business;

import com.david.notificacao.business.dto.TaskDTO;
import com.david.notificacao.infrastructure.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String from;

    @Value("${envio.email.nomeRemetente}")
    private String name;

    public void emailSender(TaskDTO taskDTO) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );
            mimeMessageHelper.setFrom(new InternetAddress(from, name));
            mimeMessageHelper.setTo(InternetAddress.parse(taskDTO.getUserEmail()));
            mimeMessageHelper.setSubject("Notificaçãr de tarefa");

            Context context = new Context();
            context.setVariable("taskName", taskDTO.getTaskName());
            context.setVariable("eventDate", taskDTO.getEventDate());
            context.setVariable("description", taskDTO.getDescription());

            String template = templateEngine.process("notificacao", context);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar o email ", e.getCause());
        }

    }
}
