package com.kb.kobo.mail.service;

import com.kb.kobo.mail.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private static final String title = "[CO_Worker] 임시 비밀번호 안내 이메일입니다.";
    private static final String message = "안녕하세요. [CO_Worker] 임시 비밀번호 안내 메일입니다. "
            + "\n" + "회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요." + "\n";

    @Value("${spring.mail.username}")
    private String from;

    public MailDto createMail(String tmpPassword, String to) {
        MailDto mailDto = new MailDto(from, to, title, message + tmpPassword);
        return mailDto;
    }

    public void sendMail(MailDto mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDto.getTo());
        mailMessage.setSubject(mailDto.getTitle());
        mailMessage.setText(mailDto.getMessage());
        mailMessage.setFrom(mailDto.getFrom());
        mailMessage.setReplyTo(mailDto.getFrom());

        mailSender.send(mailMessage);
    }
}
