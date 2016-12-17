package fr.k2i.adbeback.service.impl;

import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.AgeGroup;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.ICountryDao;
import fr.k2i.adbeback.dao.IOneTimePasswordDao;
import fr.k2i.adbeback.dao.jpa.IUserDao;
import fr.k2i.adbeback.service.UserExistsException;
import fr.k2i.adbeback.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("userManager")
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements UserManager {
    private PasswordEncoder passwordEncoder;
    private IUserDao userDao;
    @Autowired
    private ICountryDao countryDao;
    @Autowired(required = false)
    private SaltSource saltSource;
    @Autowired
    private IOneTimePasswordDao oneTimePasswordDao;


	@Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDao(IUserDao userDao) {
        this.dao = userDao;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return userDao.get(new Long(userId));
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers() {
        return userDao.getAllDistinct();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }

        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;

        user.getIdentity().setAgeGroup(AgeGroup.getGroupByBirhday(user.getIdentity().getBirthday()));

        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                final String currentPassword = userDao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                if (saltSource == null) {
                    // backwards compatibility
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    log.warn("SaltSource not set, encrypting password w/o salt");
                } else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }

        try {
            return userDao.saveUser(user);
        } catch (final Exception e) {
            e.printStackTrace();
            log.warn("err",e);
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }







/*    	if(user.getUsername() == null )return null;
    	
        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }

        if(user.getAddress()!=null && user.getAddress().getCountry()!=null && user.getAddress().getCountry().getCode()!=null){
        	try {
				Country byCode = ICountryDao.getByCode(user.getAddress().getCountry().getCode());
				user.getAddress().setCountry(byCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }

        try {
            return userDao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            //e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (JpaSystemException e) { // needed for JPA
            //e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }*/
    }


    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(new Long(userId));
    }


	public User getUserByEmail(String email) {
		return (User) userDao.loadUserByEmail(email);
		
	}


    @Transactional
    @Override
    public void changePassword(User user, String newPwd) {
        if (saltSource == null) {
            // backwards compatibility
            user.setPassword(passwordEncoder.encode(newPwd));
            log.warn("SaltSource not set, encrypting password w/o salt");
        } else {
            user.setPassword(passwordEncoder.encode(newPwd));
        }
    }



    @Transactional
    @Override
    public void changePassword(String username, String newPwd) {
        User user = userDao.findByEmailorUserName(username);
        OneTimePassword otp = oneTimePasswordDao.findBy(user, OtpAction.FORGOTTEN_PWD);
        oneTimePasswordDao.remove(otp);
        if (saltSource == null) {
            // backwards compatibility
            user.setPassword(passwordEncoder.encode(newPwd));
            log.warn("SaltSource not set, encrypting password w/o salt");
        } else {
            user.setPassword(passwordEncoder.encode(newPwd));
        }
    }

}
