package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.dao.ITransactionDao;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 29/10/13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GameController{

    public static final String ID_TRANSACTION = "ID_TRANSACTION";
    @Autowired
    private ITransactionDao transactionDao;

    @Autowired
    private MediaFacade mediaFacade;


    @RequestMapping("/game")
    public String game(Map<String, Object> model,HttpServletRequest request) {
        return "game";
    }


    @RequestMapping("/resume")
    public String resume(Map<String, Object> model,HttpServletRequest request) {
        return "manage/gooseGame/partials/resume";
    }



    @RequestMapping("/downloadMusics.html")
    public String downloadMusics(Map<String, Object> model,HttpServletRequest request) {
    //Todo:
/*
        Long idGame = (Long) request.getSession().getAttribute(ID_ADGAME);
        if(idGame==null){
            return "redirect:/";
        }
        model.put("filename",adGameFacade.getFilename(idGame));
*/
        return "cartDownload";
    }


    @RequestMapping("/downloadMusic/{idTransaction}")
    public String downloadMusics(@PathVariable Long idTransaction, Map<String, Object> model,HttpServletRequest request) {
        Transaction transaction = transactionDao.get(idTransaction);
        if(transaction.getStatus().equals(TransactionStatus.OK)){
            request.getSession().setAttribute("cart",new CartBean());
            model.put("filename",mediaFacade.getFilename(transaction));
            request.getSession().setAttribute(ID_TRANSACTION,transaction.getId());
        }else{
            return "redirect:/cart.html";
        }
        return "cartDownload";
    }


    @RequestMapping(value = "/dln/{file}.{ext}", method = RequestMethod.GET)
    public @ResponseBody
    byte[] dnl(@PathVariable String file,@PathVariable String ext,HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long idTransaction = (Long) request.getSession().getAttribute(ID_TRANSACTION);
        request.getSession().setAttribute("cart",new CartBean());
        request.getSession().setAttribute(ID_TRANSACTION,null);
        return mediaFacade.getMedias(idTransaction, response);
    }

}
