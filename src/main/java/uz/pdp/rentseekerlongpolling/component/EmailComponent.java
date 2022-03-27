package uz.pdp.rentseekerlongpolling.component;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailComponent {

    private final JavaMailSender javaMailSender;

    public void sendCode(String phone, String code) {
        try {
            phone= Objects.equals(phone, "") ?"Unknown":phone;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(phone);
            message.setTo("bekzod.m070@gmail.com");
            message.setSubject("Hay,Bekzod");
            message.setText(phone + " wants to be 'ADMIN' to your RentSeeker Bot\nCode: " + code);

            javaMailSender.send(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
