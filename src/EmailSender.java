import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.activation.*;

public class EmailSender {
    public String sendEmail(String email, String password) {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream("src/mail.properties"));

            Session mailSession = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("labaproga@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password");
            message.setText("Это ваш пароль\n" + password);

            Transport tr = mailSession.getTransport();
            tr.connect("labaproga@gmail.com", "PolinaPolina11");
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
            return "Пароль был отправлен";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "Ошибка в отпрвлении пароля";
    }
}

