package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.webapp.bean.CartBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class CheckoutController{


    @Value("${addonf.paiement.url:http://127.0.0.1:8080/game}")
    private String paymentUrl;


    @ModelAttribute("player")
    public User player(){
        return new User();
    }

    @RequestMapping("/checkout.html")
    public String checkout(Map<String, Object> model,HttpServletRequest request) throws Exception {
        CartBean cart = (CartBean) request.getSession().getAttribute("cart");
        model.put("paymentUrl",paymentUrl);
        if(cart.getNbProduct()==0){
            return "redirect:/cart.html";
        }
        return "checkout";
    }


}
