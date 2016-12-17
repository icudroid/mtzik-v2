package fr.k2i.adbeback.webapp.facade;

import com.mysema.query.jpa.JPAExpressions;
import com.mysema.query.types.expr.StringExpression;
import fr.k2i.adbeback.application.services.mail.IMailEngine;
import fr.k2i.adbeback.application.services.mail.dto.Email;
import fr.k2i.adbeback.application.services.mail.exception.SendException;
import fr.k2i.adbeback.audio.AudioTools;
import fr.k2i.adbeback.core.business.country.City;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.*;
import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.dao.*;
import fr.k2i.adbeback.dao.jpa.IProfileRepository;
import fr.k2i.adbeback.dao.jpa.IUserBoDao;
import fr.k2i.adbeback.core.business.filter.UserFilter;
import fr.k2i.adbeback.webapp.bean.ArtistBean;
import fr.k2i.adbeback.webapp.bean.RegisterBoBean;
import fr.k2i.adbeback.webapp.bean.TypeRegistration;
import fr.k2i.adbeback.webapp.bean.UserBean;
import fr.k2i.adbeback.webapp.bean.builder.user.UserBeanBuilder;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableOrder;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableSearch;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import fr.k2i.adbeback.webapp.command.UserCommand;
import fr.k2i.adbeback.webapp.dto.ArtistDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 08/11/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserFacade {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    private ICityDao cityDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IUserBoDao userBoDao;

    @Autowired
    private IMailEngine mailEngine;

    @Autowired
    private DESCryptoService desCryptoService;

    @Value("${addonf.base.url}")
    private String urlBase;

    @Autowired
    private IOneTimePasswordDao oneTimePasswordDao;

    @Autowired
    private IMediaDao mediaDao;

    @Autowired
    private ICategoryDao categoryDao;

    @Value("${addonf.music.location:/musics/}")
    private String musicPath;

    @Value("${addonf.music.sample.location}")
    private String samplePath;

    @Autowired
    private IArtistDao artistDao;



    //http://demo.mtzik.fr/static/sample/150.mp3

    @Autowired
    private AudioTools audioTools;



    @Transactional
    public User getCurrentUser() {
        BOUser principal = (BOUser) getAuthenticationPlayer().getPrincipal();

        return userBoDao.get(principal.getUser().getId());

/*
        if (principal instanceof WebUserArtist) {
            return userBoDao.get(((WebUserArtist) principal).getUser().getId());
        }else if (principal instanceof WebUserLabel) {
            return userBoDao.get(((WebUserLabel) principal).getUser().getId());
        }else{
            throw new AssertionError("Please check configuration. Should be User in the principal.");
        }
*/

    }

    protected Authentication getAuthenticationPlayer() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @Transactional
    public void createAccount(RegisterBoBean registerArtistBean){

        if(TypeRegistration.LABEL.equals(registerArtistBean.getTypeRegistration())){
            User userLabel  = new User();

            Productor productor = new Productor();
            productor.setLabelName(registerArtistBean.getName());
            productor.setFirstName(registerArtistBean.getFirstname());
            productor.setLastName(registerArtistBean.getLastname());
            userLabel.setIdentity(productor);

            Address addressArtist = new Address();
            addressArtist.setAddress(registerArtistBean.getAddress());
            City city = cityDao.findByZipcodeAndCityAndCountry_Code(registerArtistBean.getZipCode(), registerArtistBean.getCity(), registerArtistBean.getCountry());
            addressArtist.setCity(city);
            userLabel.getIdentity().setAddress(addressArtist);

            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
            builder .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral("/")
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral("/")
                    .appendValue(ChronoField.YEAR);
            DateTimeFormatter formatter = builder.toFormatter();
            LocalDate birthday = LocalDate.parse(registerArtistBean.getBirthDate(), formatter);

            userLabel.getIdentity().setBirthday(birthday);

            userLabel.setPassword(passwordEncoder.encode(registerArtistBean.getPassword()));
            userLabel.setEmail(registerArtistBean.getEmail());
            //userLabel.setNewsletter();
            userLabel.getIdentity().setPhone(registerArtistBean.getPhone());
            userLabel.addProfile(profileRepository.findByName(IProfileRepository.LABEL_PROFILE));
            userLabel.setUsername(registerArtistBean.getUsername());


            sendNewLabelAccount(userBoDao.save(userLabel));
        }else{
            User userArtist  = new User();

            Artist artist = new Artist();
            artist.setArtistName(registerArtistBean.getName());
            artist.setFirstName(registerArtistBean.getFirstname());
            artist.setLastName(registerArtistBean.getLastname());
            userArtist.setIdentity(artist);

            Address addressArtist = new Address();
            addressArtist.setAddress(registerArtistBean.getAddress());
            City city = cityDao.findByZipcodeAndCityAndCountry_Code(registerArtistBean.getZipCode(), registerArtistBean.getCity(), registerArtistBean.getCountry());
            addressArtist.setCity(city);
            userArtist.getIdentity().setAddress(addressArtist);

            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
            builder .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral("/")
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral("/")
                    .appendValue(ChronoField.YEAR);
            DateTimeFormatter formatter = builder.toFormatter();
            LocalDate birthday = LocalDate.parse(registerArtistBean.getBirthDate(), formatter);

            userArtist.getIdentity().setBirthday(birthday);

            userArtist.setPassword(passwordEncoder.encode(registerArtistBean.getPassword()));
            userArtist.setEmail(registerArtistBean.getEmail());
            //userArtist.setNewsletter();
            userArtist.getIdentity().setPhone(registerArtistBean.getPhone());
            userArtist.addProfile(profileRepository.findByName(IProfileRepository.ARTIST_LABEL));
            userArtist.setUsername(registerArtistBean.getUsername());


            sendNewArtistAccount(userBoDao.save(userArtist));
        }




    }

    private void sendNewLabelAccount(User user) {
        log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");

        Map<String, Object> modelEmail = new HashMap<String, Object>();

        String endUrl = desCryptoService.generateOtp(user.getEmail() + '|' + user.getUsername(), user, OtpAction.CREATE_ACCOUNT);

        modelEmail.put("url",urlBase+"account/confirmCreateAccount/"+endUrl);

        modelEmail.put("username", user.getUsername());
        modelEmail.put("labelname", ((Productor)user.getIdentity()).getLabelName());
        modelEmail.put("email", user.getEmail());

        Email.Producer producer = Email.builder()
                .subject("Welcome")
                .model(modelEmail)
                .content("email/bo-artist/welcome-label")
                .recipients(user.getEmail())
                .noAttachements();

        try {
            mailEngine.sendMessage(producer.build(), Locale.FRANCE);
        } catch (Exception e) {
            log.debug("Une erreur est survenu leur de l'envoie de l'email", e);
        }
    }

    private void sendNewArtistAccount(User user) {
        log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");

        Map<String, Object> modelEmail = new HashMap<String, Object>();

        String endUrl = desCryptoService.generateOtp(user.getEmail() + '|' + user.getUsername(), user, OtpAction.CREATE_ACCOUNT);

        modelEmail.put("url",urlBase+"account/confirmCreateAccount/"+endUrl);

        modelEmail.put("username", user.getUsername());
        modelEmail.put("artistname", ((Artist)user.getIdentity()).getArtistName());
        modelEmail.put("email", user.getEmail());

        Email.Producer producer = Email.builder()
                .subject("Welcome")
                .model(modelEmail)
                .content("email/bo-artist/welcome-artist")
                .recipients(user.getEmail())
                .noAttachements();

        try {
            mailEngine.sendMessage(producer.build(), Locale.FRANCE);
        } catch (Exception e) {
            log.debug("Une erreur est survenu leur de l'envoie de l'email", e);
        }
    }




    @Transactional
    public void sendForgottenPasswd(String email,HttpServletRequest request) throws SendException {

        User user = userBoDao.getUserByEmail(email);

        if(user != null){

            Map<String, Object> modelEmail = new HashMap<String, Object>();
            String endUrl = desCryptoService.generateOtp(user.getUsername(), user, OtpAction.FORGOTTEN_PWD);
            modelEmail.put("name",user.getUsername());
            modelEmail.put("url",urlBase+"pwdinit/"+endUrl);
            Email forgottenPasswd = Email.builder()
                    .subject("Mot de passe oublié")
                    .model(modelEmail)
                    .content("email/forgottenPasswd")
                    .recipients(user.getEmail())
                    .noAttachements()
                    .build();

            try{
                mailEngine.sendMessage(forgottenPasswd,request.getLocale());
            }catch (Exception e){

            }
        }


    }



    @Transactional
    public boolean isValidateOtp(String username, String key){
        User user = userBoDao.getUserByEmail(username);
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
        User user = userBoDao.getUserByEmail(username);
        userBoDao.setPassword(user.getId(), password);
        OneTimePassword otp = oneTimePasswordDao.findBy(user, OtpAction.FORGOTTEN_PWD);
        oneTimePasswordDao.remove(otp);
    }


    @Transactional
    public void deleteArtist(Long idArtist) throws Exception {
        User user = getCurrentUser();
        Artist artist = mediaDao.findArtistById(idArtist);
        if (user.getIdentity() instanceof Productor) {
            if(artist.getProductors().contains(user.getIdentity())){
                artistDao.remove(artist);
            }else{
                throw new Exception("Bad user");
            }
        }else{
            throw new Exception("Bad user");
        }


    }

    @Transactional
    public void addArtist(ArtistDTO artistDTO) throws Exception {

        User user = getCurrentUser();
        if (user.getIdentity() instanceof Productor) {

            Artist artist = new Artist();
            artist.setMainProductor((Productor) user.getIdentity());

            artist.setLastName(artistDTO.getLastName());
            artist.setArtistName(artistDTO.getArtistName());
            artist.setFirstName(artistDTO.getFirstName());
            artist.setBiography(artistDTO.getBiography());
            artist.setFacebook(artistDTO.getFacebook());
            artist.setGooglePlus(artistDTO.getGooglePlus());
            artist.setTwitter(artistDTO.getTwitter());
            artist.setWebsite(artistDTO.getWebsite());

            String uuid = UUID.randomUUID().toString();

            String originalName = artistDTO.getPhoto().getOriginalFilename();

            int dot = originalName.lastIndexOf(".");
            String ext = originalName.substring(dot + 1);

            String localFile = uuid + "." + ext;

            String filename = musicPath + localFile;

            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(artistDTO.getPhoto().getBytes());
            fos.close();

            artist.setPhoto(localFile);

            artistDao.save(artist);


        }else{
            throw new Exception("Bad user");
        }



    }

    @Transactional
    public void modArtist(Long id, ArtistDTO artistDTO) throws Exception {
        Artist artist = artistDao.get(id);


        User user = getCurrentUser();

        if (user.getIdentity() instanceof Productor) {
            Productor productor = (Productor) user.getIdentity();
            if(!artistDao.isMainProductor(artist,productor)){
                throw new Exception("Bad user");
            }
        }else if (user.getIdentity() instanceof Artist) {
            if(!user.getIdentity().getId().equals(id)){
                throw new Exception("Bad user");
            }
        }

        artist.setLastName(artistDTO.getLastName());
        artist.setArtistName(artistDTO.getArtistName());
        artist.setFirstName(artistDTO.getFirstName());
        artist.setBiography(artistDTO.getBiography());
        artist.setFacebook(artistDTO.getFacebook());
        artist.setGooglePlus(artistDTO.getGooglePlus());
        artist.setTwitter(artistDTO.getTwitter());
        artist.setWebsite(artistDTO.getWebsite());

        String uuid = UUID.randomUUID().toString();

        String originalName = artistDTO.getPhoto().getOriginalFilename();

        int dot = originalName.lastIndexOf(".");
        String ext = originalName.substring(dot + 1);

        String localFile = uuid + "." + ext;

        String filename = musicPath + localFile;

        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(artistDTO.getPhoto().getBytes());
        fos.close();

        artist.setPhoto(localFile);

        artistDao.save(artist);


    }

    public List<ArtistBean> getListArtist() throws Exception {

        User user = getCurrentUser();

        if (user.getIdentity() instanceof Productor) {
            List<Artist> artists = artistDao.findByLabel((Productor) user.getIdentity());
            List<ArtistBean> artistBeans = new ArrayList<>();
            for (Artist artist : artists) {
                artistBeans.add(new ArtistBean(artist));
            }
            return artistBeans;
        }else
            throw new Exception("Bad user");

    }

    public List<ArtistBean> getListArtist(String query) throws Exception {
        User user = getCurrentUser();

        if (user.getIdentity() instanceof Productor) {
            List<Artist> artists = artistDao.findByLabel((Productor) user.getIdentity(),query);
            List<ArtistBean> artistBeans = new ArrayList<>();
            for (Artist artist : artists) {
                artistBeans.add(new ArtistBean(artist));
            }
            return artistBeans;
        }else
            throw new Exception("Bad user");
    }

    public ArtistBean findArtistById(Long id) {
        return new ArtistBean(artistDao.get(id));
    }

    @Transactional
    public List<User> findAllUsers() {
        return userBoDao.getAll();
    }

    public DataTableResponse<UserBean> findByQuery(DataTableQuery<UserCommand> query, List<String> relationObject) {
        DataTableResponse<UserBean> res = new DataTableResponse();
        res.setDraw(query.getDraw());

        DataTableSearch searchObj = query.getSearch();
        String value = searchObj.getValue();


        Integer length = query.getLength();
        Integer start = query.getStart();
        List<Sort.Order> orders = new ArrayList<>();


        List<DataTableOrder> orderObj = query.getOrder();
        orders.addAll(orderObj.stream().map(dataTableOrder -> new Sort.Order(dataTableOrder.toDirection(), relationObject.get(dataTableOrder.getColumn()))).collect(Collectors.toList()));
        Sort sort = new Sort(orders);

        //StringExpression type = JPAExpressions.type().asc();


        Pageable pageable = new PageRequest(start/length,length,sort);

        UserCommand additionalForm = query.getAdditionalForm();

        UserFilter filter = additionalForm.toFilter();

        Page<User> users = userBoDao.findUsers(filter, sort, pageable);

        List<UserBean> content = UserBeanBuilder.build(users.getContent());

        res.setData(content);
        res.setRecordsFiltered(users.getTotalElements());
        res.setRecordsTotal(users.getTotalElements());

        return res;
    }
}
