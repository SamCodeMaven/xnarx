package uz.xnarx.productservice.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.xnarx.productservice.exception.CustomException;
import uz.xnarx.productservice.payload.enums.ErrorCode;

/**
 * @author Samandar Daminov
 * Date: 10/24/2024
 */

@Service
@Slf4j
@AllArgsConstructor
public class EmailService implements EmailSender{

    private JavaMailSender mailSender;


    @Async
    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("hello@amigoscode.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error("FAILED TO SEND EMAIL", e);
            throw new CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE);
        }
    }
}
