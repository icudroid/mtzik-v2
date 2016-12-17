package fr.k2i.adbeback.webapp.controller.ajax;

import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.ITransactionDao;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.bean.MediaLineBean;
import fr.k2i.adbeback.webapp.bean.TransactionInformation;
import fr.k2i.adbeback.webapp.facade.CartFacade;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
public class CartWebservice {
    @Autowired
    private CartFacade cartFacade;

    @Value(value ="${addonf.static.url}" )
    private String staticUrl;

    @Value(value = "${addonf.max.in.cart:3}")
    private Integer maxMusicInCart;

    @Value(value = "${addonf.max.in.cart.no.loggued:1}")
    private Integer maxMusicUnloggued;

    public static final String ID_TRANSACTION = "ID_TRANSACTION";
    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private MediaFacade mediaFacade;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserFacade userFacade;


    @RequestMapping(value="/partial/cart.html",method = RequestMethod.GET)
    public
    String cart(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws Exception {
        CartBean cart = getCart(request, response);
        model.put("cart",cart);
        return "layout/fragment/cart";
    }


    @RequestMapping(value="/partial/cartTable.html",method = RequestMethod.GET)
    public
    String cartTable(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws Exception {
        CartBean cart = getCart(request, response);
        model.put("cart",cart);
        model.put("staticUrl",staticUrl);
        return "layout/fragment/cartTable";
    }


    @RequestMapping(value="/rest/addToCart/{musicId}",method = RequestMethod.GET)
    public  @ResponseBody
    CartBean addToCart(@PathVariable Long musicId , HttpServletRequest request, HttpServletResponse response) throws Exception {
    	CartBean cart = getCart(request, response);
    	cart.setError("");
        User user = null;
        try {
            user = userFacade.getCurrentPlayer();
        }catch (AssertionError assertionError){}


        int maxMusic = maxMusicUnloggued;

        if(user!=null){
            maxMusic=maxMusicInCart;
        }

        if(cart.getNbProduct()>=maxMusic){
    		cart.setError(messageSource.getMessage("addonf.cart.max",new Object[]{maxMusic},request.getLocale()));
    	}else{
	    	MediaLineBean line = cartFacade.getMediaLineBean(musicId);
	    	if(cart.getLines().contains(line)){
                cart.setError(messageSource.getMessage("addonf.cart.alreadyAdded",new Object[]{maxMusicInCart},request.getLocale()));
	    	}else{
	    		cart.getLines().add(line);
                cart.setError(messageSource.getMessage("addonf.cart.added",new Object[]{maxMusicInCart},request.getLocale()));
	    	}
	    	recalculateAdNeeeded(cart);
    	}
    	
    	return cart;
    }


    private void recalculateAdNeeeded(CartBean cart){
    	Set<MediaLineBean> lines = cart.getLines();
    	Integer nb = 0;
    	Integer nbMedias = 0;
		for (MediaLineBean mediaLineBean : lines) {
            nb+=mediaLineBean.getAdNeeded();
            nbMedias++;
		}
		cart.setNbProduct(nbMedias);
		cart.setMinScore(nb);
    }

    @RequestMapping(value="/rest/removeFromCart/{musicId}",method = RequestMethod.GET)
    public  @ResponseBody
    CartBean removeFromCart(@PathVariable Long musicId,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	CartBean cart = getCart(request, response);
    	cart.setError("");
    	Set<MediaLineBean> lines = cart.getLines();
    	MediaLineBean toRemove = null;
		for (MediaLineBean mediaLineBean : lines) {
			if(mediaLineBean.getIdMedia().equals(musicId)){
				toRemove = mediaLineBean;
			}
		}
		lines.remove(toRemove);
		recalculateAdNeeeded(cart);
    	return cart;
    }

    
    @RequestMapping(value="/rest/cart",method = RequestMethod.GET)
    public  @ResponseBody
    CartBean getCart(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	CartBean cart = (CartBean) request.getSession().getAttribute("cart");
    	if(cart==null){
    		cart = new CartBean();
    		request.getSession().setAttribute("cart",cart);
    	}
    	return cart;
    }

    @RequestMapping(value="/rest/cart/empty",method = RequestMethod.GET)
    public  @ResponseBody
    String empty(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("cart");
    	return null;
    }


    @RequestMapping(value="/rest/createTr",method = RequestMethod.GET)
    public @ResponseBody TransactionInformation validateCart(HttpServletRequest request) throws Exception {
        return cartFacade.createTransaction(request);
    }


    @RequestMapping(value="/rest/validate",method = RequestMethod.POST)
    public @ResponseBody WSResponse validateTransaction(HttpServletRequest request) throws Exception {

        String idTransaction = request.getParameter("idTransaction");
        String status = request.getParameter("status");
        Long transaction = Long.valueOf(idTransaction);
        if("ok".equals(status)){
            cartFacade.validateTransaction(transaction);
        }else{
            cartFacade.cancelTransaction(transaction);
        }
        return new WSResponse();
    }

    @Data
    class WSResponse implements  Serializable{
        private String msg;
    }



    @RequestMapping(value = "/rest/paymentReturn", method = RequestMethod.POST)
    public String paymentReturn(Map<String, Object> model,HttpServletRequest request) throws Exception {
        Transaction transaction = transactionDao.get(Long.valueOf(request.getParameter("idTransaction")));
        if(transaction.getStatus().equals(TransactionStatus.OK)){
            request.getSession().setAttribute("cart",new CartBean());
            model.put("filename",mediaFacade.getFilename(transaction));
            request.getSession().setAttribute(ID_TRANSACTION,transaction.getId());
        }else{
            return "redirect:/cart.html";
        }

        return "cartDownload";

    }

}
