package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.dao.ITransactionDao;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 29/10/13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CartController{

    @Value("${addonf.paiement.url:http://127.0.0.1:8080/game}")
    private String paymentUrl;




    @RequestMapping("/cart.html")
    public String cart(Map<String, Object> model,HttpServletRequest request) throws Exception {
        model.put("paymentUrl",paymentUrl);
        return "cart";
    }




}
