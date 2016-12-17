package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.dao.ITransactionDao;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.bean.MediaLineBean;
import fr.k2i.adbeback.webapp.bean.TransactionInformation;
import fr.k2i.adbeback.webapp.bean.information.CountryInformation;
import fr.k2i.adbeback.webapp.bean.information.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 06/01/14
 * Time: 00:33
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CartFacade {

    @Value("${mtzik.password:a123456}")
    private String PASSWORD;

    @Autowired
    private IMediaDao mediaDao;

    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private UserFacade userFacade;

    @Value("${addonf.paiement.callSys:http://localhost:8180/rest/validate}")
    private String paymentCallSysUrl;

    @Value("${addonf.paiement.callBack:http://localhost:8180/rest/paymentReturn}")
    private String paymentCallBackUrl;

    @Value("${addonf.paiement.idPartner}")
    private String idPartner;

    @Value("${addonf.paiement.adPrice:0.25}")
    private Double adPrice;


    @Transactional
    public MediaLineBean getMediaLineBean(Long idMedia) throws Exception {
        Media media = mediaDao.get(idMedia);

        MediaLineBean line = new MediaLineBean();

        line.setIdMedia(idMedia);
        line.setTitle(media.getTitle());
        line.setAdNeeded(media.getNbAdsNeeded());

        if(StringUtils.isEmpty(media.getJacket())){
            line.setJacket("no.png");
        }else {
            line.setJacket(media.getJacket());
        }

        return line;
    }

    @Transactional
    public TransactionInformation createTransaction(HttpServletRequest request) throws UnsupportedEncodingException {
        TransactionInformation res = new TransactionInformation();
        CartBean cart = (CartBean) request.getSession().getAttribute("cart");
        Transaction tr = new Transaction();
        Set<MediaLineBean> lines = cart.getLines();

        List<Media> medias = new ArrayList<Media>();
        Integer nbAds = 0;
        for (MediaLineBean line : lines) {
            nbAds+=line.getAdNeeded();
            medias.add(mediaDao.get(line.getIdMedia()));
        }

        tr.setAmount(nbAds*adPrice);
        tr.setMedias(medias);
        tr.setStatus(TransactionStatus.IN_PROGRESS);

        tr = transactionDao.save(tr);
        res.setIdTransaction(tr.getId().toString());
        res.setAmount(tr.getAmount());

        res.setCallSysUrl(paymentCallSysUrl);
        res.setCallBackUrl(paymentCallBackUrl);
        res.setSelfAd(false);
        res.setIdPartner(idPartner);
        Date trDate = new Date();
        res.setTransactionDate(trDate);

        String encode = PASSWORD+trDate.getTime()+res.getIdTransaction();
        String hex = DigestUtils.md5DigestAsHex(encode.getBytes("UTF-8"));
        res.setValidation(hex);
        res.setCurrencyCode("EUR");

        Information infos = new Information();
        infos.setCountry(new CountryInformation("FR"));
        res.setInformations(infos);

        Authentication authentication = userFacade.getAuthenticationPlayer();
        if(!authentication.getPrincipal().equals("anonymousUser")){
            User user = userFacade.getCurrentPlayer();
            tr.setUser(user);
            //user.addTransaction(tr);
        }


        return res;
    }

    @Transactional
    public void validateTransaction(Long idTransaction) {
        Transaction transaction = transactionDao.get(idTransaction);
        if(transaction.getStatus().equals(TransactionStatus.IN_PROGRESS)){
            transaction.setStatus(TransactionStatus.OK);
        }
    }

    @Transactional
    public void cancelTransaction(Long idTransaction) {
        Transaction transaction = transactionDao.get(idTransaction);
        if(transaction.getStatus().equals(TransactionStatus.IN_PROGRESS)){
            transaction.setStatus(TransactionStatus.CANCEL);
        }
    }

}
