package com.zenika.zencontact.resource.email;

import com.zenika.zencontact.domain.Email;
import com.zenika.zencontact.resource.auth.AuthenticationService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by PierreG on 01/03/17.
 *
 */
public class EmailService {

    private static final Logger LOG = Logger.getLogger(EmailService.class.getSimpleName());
    private static EmailService INSTANCE = new EmailService();

    public static EmailService getInstance() {
        return INSTANCE;
    }


    public void sendEmail(Email email) {
        LOG.warning("Send email: " + email);

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(
                    AuthenticationService.getInstance().getUser().getEmail(),
                    AuthenticationService.getInstance().getUsername())
            );
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.to, email.toName));
            msg.setReplyTo(new Address[]{
                    new InternetAddress("team@tp-imta-pgeff.appspotmail.com", "Application Contact")
            });
            msg.setSubject(email.subject);
            msg.setText(email.body);

            Transport.send(msg);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.severe(e.getMessage());
        }
    }

    public void logEmail(HttpServletRequest request) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            //MimeMessage encapsule toutes les informations
            MimeMessage message = new MimeMessage(session,
                    request.getInputStream());

            LOG.warning("Subject:" + message.getSubject()); //Sujet du mail

            Multipart multipart = (Multipart) message.getContent();
            BodyPart part = multipart.getBodyPart(0);
            LOG.warning("Body:" + part.getContent()); //Contenu du mail

            for (Address sender : message.getFrom()) {
                LOG.warning("From:" + sender.toString()); //Origine du mail
            }
        } catch (MessagingException | IOException e) {
        }
    }
}
