package fr.k2i.adbeback.application.services.mail;


import fr.k2i.adbeback.application.services.mail.dto.Email;
import fr.k2i.adbeback.application.services.mail.exception.SendException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 04/10/13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public interface IMailEngine {
    void sendMessage(Email email, Locale locale) throws SendException;

    void sendMessage(Email email) throws SendException;
}
