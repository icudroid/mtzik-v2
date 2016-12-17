package fr.k2i.adbeback.crypto;

import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IOneTimePasswordDao;
import fr.k2i.adbeback.logger.LogHelper;
import org.apache.commons.lang.math.RandomUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.spec.KeySpec;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Simple service that encrypts data with the DES algorithm and a passphrase. <br/>
 * Spring initialized, statically callable
 * 
 * @author lchen
 * 
 */
@Component
public class DESCryptoService {


    private static Logger logger = LogHelper.getLogger(DESCryptoService.class);

    @Value(value = "${DES.passphrase}")
    private String passphrase;



    @Autowired
    private IOneTimePasswordDao oneTimePasswordDao;


    private static byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
        };

    public String encrypt(String stringToEncrypt) {
        try {
            Cipher cipher = newCipher(Cipher.ENCRYPT_MODE);
            byte[] b = cipher.doFinal(stringToEncrypt.getBytes());
            // return Base64.encodeBase64String(b);
            return new String(Hex.encode(b), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error while encrypting data: " + stringToEncrypt, e);
        } catch (GeneralSecurityException e) {
            logger.error("Error while encrypting data: " + stringToEncrypt, e);
        }
        return null;
    }

    public String decrypt(String stringToDecrypt) {
        try {
            Cipher cipher = newCipher(Cipher.DECRYPT_MODE);
            byte[] b = cipher.doFinal(Hex.decode(stringToDecrypt));
            return new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error while decrypting data: " + stringToDecrypt, e);
        } catch (GeneralSecurityException e) {
            logger.error("Error while decrypting data: " + stringToDecrypt, e);
        }
        return null;
    }

    /**
     * Shortcut for URLEncoder.encode(encrypt(stringToEncode)) <br/>
     * May return null if UTF-8 is not supported (<-- big threat here)
     */
    public String encryptAndUrlEncode(String toProcess) {
        try {
            return URLEncoder.encode(encrypt(toProcess), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error while url encoding encrypted data: " + toProcess, e);
            return null;
        }
    }

    private Cipher newCipher(int mode) throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
        KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 19);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES", "BC").generateSecret(keySpec);
        cipher.init(mode, key);
        return cipher;
    }


    @Transactional
    public String generateOtp(String toEncodeStr, User user, OtpAction action){
        OneTimePassword otp =  oneTimePasswordDao.findBy(user,action);

        if(otp==null){
            NumberFormat numberFormat = new DecimalFormat("0000000");
            String key = numberFormat.format(RandomUtils.nextInt(100000000));
            otp = new OneTimePassword();
            otp.setKey(key);
            otp.setAction(action);
            otp.setUser(user);
            oneTimePasswordDao.save(otp);
        }

        return encrypt(toEncodeStr)+"/"+otp.getKey();
    }
}
