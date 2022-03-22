package uz.pdp.rentseekerlongpolling.component;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailComponent {

    private final JavaMailSender javaMailSender;

    public void sendCode(String phone, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(phone);
            message.setTo("bekzod.m070@gmail.com");
            message.setSubject("Hay,Bekzod");
            message.setText(phone + " wants to be 'ADMIN' to your RentSeeker Bot\nCode: " + code);

            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
