package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.dao.IOneTimePasswordDao;
import fr.k2i.adbeback.dao.jpa.IUserBoDao;
import fr.k2i.adbeback.logger.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dimitri
 * Date: 10/04/14
 * Time: 14:42
 * Goal:
 */
@Component
public class OtpServiceFacade {
    private Logger logger = LogHelper.getLogger(this.getClass());
    @Autowired
    private DESCryptoService desCryptoService;

    @Autowired
    private IOneTimePasswordDao oneTimePasswordDao;

    @Autowired
    private IUserBoDao userBoDao;

    @Transactional
    public void removeOtpAndValidateUser(String email, String name, String code) {
        User user = userBoDao.getUserByEmail(email);
        OneTimePassword otp = oneTimePasswordDao.findBy(user, OtpAction.CREATE_ACCOUNT);
        oneTimePasswordDao.remove(otp);

        userBoDao.enable(user.getId());

    }


    public enum ConfirmationRegistration {
        OK,KO;
    }

    @Transactional
    public ConfirmationRegistration confirmRegistration(String email, String name, String code) {

        User user = userBoDao.getUserByEmail(email);

        OneTimePassword otp = oneTimePasswordDao.findBy(user, OtpAction.CREATE_ACCOUNT);
        if(otp == null){
            return ConfirmationRegistration.KO;
        }else if(otp.getKey().equals(code)){
                return ConfirmationRegistration.OK;
        }else{
            return ConfirmationRegistration.KO;
        }
    }
}
