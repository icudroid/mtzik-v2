package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.application.services.mail.IMailEngine;
import fr.k2i.adbeback.application.services.mail.dto.Email;
import fr.k2i.adbeback.application.services.mail.exception.SendException;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.core.business.user.BOUser;
import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.dao.IOneTimePasswordDao;
import fr.k2i.adbeback.dao.jpa.IProfileRepository;
import fr.k2i.adbeback.dao.jpa.IUserDao;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.logger.Log;
import fr.k2i.adbeback.service.UserExistsException;
import fr.k2i.adbeback.service.UserManager;
import fr.k2i.adbeback.webapp.bean.myaccount.ChangePwdBean;
import fr.k2i.adbeback.webapp.command.EnrollCommand;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 08/11/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserFacade {

    @Log
    private Logger logger;

    @Autowired
    private IUserDao playerDao;

    @Autowired
    private DESCryptoService desCryptoService;

    @Autowired
    private IOneTimePasswordDao oneTimePasswordDao;

    @Autowired
    private IMailEngine mailEngine;

    @Autowired
    private UserManager playerManager;

    @Value("${addonf.base.url}")
    private String urlBase;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IMediaDao mediaDao;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IProfileRepository profileRepository;

    @Transactional
    public User getCurrentPlayer() {
        Object principal = getAuthenticationPlayer().getPrincipal();
        if (!(principal instanceof BOUser)) {
            throw new AssertionError("Please check configuration. Should be User in the principal.");
        }

        return playerDao.get(((BOUser) principal).getUser().getId());
    }

    protected Authentication getAuthenticationPlayer() {
        return SecurityContextHolder.getContext().getAuthentication();
    }




    @Transactional
    public void sendForgottenPasswd(String username,HttpServletRequest request) throws SendException {
        User user = playerDao.findByEmailorUserName(username);

        if(user != null){

            Map<String, Object> modelEmail = new HashMap<String, Object>();
            String endUrl = desCryptoService.generateOtp(user.getUsername(),user, OtpAction.FORGOTTEN_PWD);
            modelEmail.put("name",user.getUsername());
            modelEmail.put("url",urlBase+"pwdinit/"+endUrl);
            Email forgottenPasswd = Email.builder()
                    .subject("Mot de passe oublié")
                    .model(modelEmail)
                    .content("email/forgottenPasswd")
                    .recipients(user.getEmail())
                    .noAttachements()
                    .build();

            mailEngine.sendMessage(forgottenPasswd,request.getLocale());
        }

    }



    @Transactional
    public boolean isValidateOtp(String username, String key){
        User user = playerDao.findByEmailorUserName(username);
        if(user ==null){
            return false;
        }
        OneTimePassword otp = oneTimePasswordDao.findBy(user, OtpAction.FORGOTTEN_PWD);
        if(otp == null){
            return false;
        }
        if(!otp.getKey().equals(key)){
            return false;
        }

        return true;
    }


    @Transactional
    public void changePasswd(String username, String password) {
        playerManager.changePassword(username, password);
    }

    @Transactional
    public void changePasswd(String password) {
        playerManager.changePassword(getCurrentPlayer(), password);
    }

    public void validateChangePwdBean(ChangePwdBean changePwdBean, Errors errors,Locale locale) {

        if(!errors.hasErrors()){
            String password = getCurrentPlayer().getPassword();
            String old = passwordEncoder.encode(changePwdBean.getOldPassword());
            if(!password.equals(old)){
                errors.rejectValue("oldPassword","notMatch",messageSource.getMessage("addonf.notMatch",null,locale));
            }

            if(!changePwdBean.getNewConfirmPassword().equals(changePwdBean.getNewPassword())){
                errors.rejectValue("newConfirmPassword","notMatch",messageSource.getMessage("addonf.notMatch",null,locale));
            }
         }

    }

    public Page<Media> getDownloadedMusic(long genreId, String req, Pageable pageable) {
        return mediaDao.getDownloadedMusic(getCurrentPlayer(),genreId,req,pageable);
    }

    public void subscribeUser(EnrollCommand user, Locale locale) throws UserExistsException {
        User u = User.Mtzik_User();

        u.getIdentity().setAddress(user.getAddress());
        u.getIdentity().setBirthday(DateUtils.asLocalDate(user.getBirthday()));
        u.getIdentity().setSex(user.getSex());

        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());


        u.setEnabled(true);

        // Set the default user role on this new user
        u.addProfile(profileRepository.findByName(IProfileRepository.MTZIK_PROFILE));

        // unencrypted users password to log in user automatically
        final String password = user.getPassword();

        playerManager.saveUser(u);


        // Send user an e-mail
        if (logger.isDebugEnabled()) {
            logger.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }

        Map<String, Object> modelEmail = new HashMap<String, Object>();
        modelEmail.put("user", user.getUsername());

        Email.Producer producer = Email.builder()
                .subject("Welcome")
                .model(modelEmail)
                .content("email/welcome")
                .recipients(user.getEmail())
                .noAttachements();

        try {
            mailEngine.sendMessage(producer.build(), Locale.FRANCE);
        } catch (Exception e) {
            logger.debug("Une erreur est survenu leur de l'envoie de l'email", e);
        }



    }
}
