package com.david.notificacao.controller;

import com.david.notificacao.business.EmailService;
import com.david.notificacao.business.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody TaskDTO taskDTO) {
        emailService.emailSender(taskDTO);
        return ResponseEntity.ok().build();
    }
}
