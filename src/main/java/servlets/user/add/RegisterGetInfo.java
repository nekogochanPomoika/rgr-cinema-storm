package servlets.user.add;

import handler.MailSender;
import handler.NewUserInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet(urlPatterns = "/register-get-info")
public class RegisterGetInfo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewUserInfo.login = req.getParameter("login");
        NewUserInfo.mail = req.getParameter("mail");
        NewUserInfo.password = req.getParameter("password");

        String toEmail = NewUserInfo.mail;
        String fromEmail = "cimena-storm@rgr.com";
        String subject = "Cinema-storm mail validation";
        String text;
        String link;

        String keyChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        char[] chars = keyChars.toCharArray();
        Random random = new Random();

        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            key.append(chars[random.nextInt(chars.length)]);
        }

        link = "http://localhost:8080/rgr_cinema_storm_war_exploded/register-end.html?key=" + key;

        text = "Чтобы создать аккаунт перейдите по ссылке ниже\n\t" + link;

        NewUserInfo.key = key.toString();

        MailSender sslSender = new MailSender("mail@example.com", "password1234");
        sslSender.send(subject, text, fromEmail, toEmail);
    }
}
