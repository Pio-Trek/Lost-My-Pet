package graded.unit.lostmypetrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service layer which manages sending emails with tokens.
 *
 * @author Piotr Przechodzki
 */
@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Method that is respond for sending email message using
     * {@link JavaMailSender} interface.
     *
     * @param email This is a simple mail message model including data
     *              such as the from, to, subject, and text fields.
     */
    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}