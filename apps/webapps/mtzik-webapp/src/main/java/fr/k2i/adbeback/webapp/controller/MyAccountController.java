package fr.k2i.adbeback.webapp.controller;

import com.google.common.net.HttpHeaders;
import fr.k2i.adbeback.dao.ICategoryDao;
import fr.k2i.adbeback.webapp.bean.myaccount.ChangePwdBean;
import fr.k2i.adbeback.webapp.bean.search.SearchCommand;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 18/01/14
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MyAccountController {

    @Autowired
    private UserFacade playerFacade;

    @Autowired
    private ICategoryDao categoryDao;

    @Autowired private MediaFacade mediaFacade;

    @Autowired
    private Validator validator;

    @RequestMapping(value = "/redownload")
    public String redownload(@ModelAttribute("search")SearchCommand searchCommand, Pageable pageable, Map<String, Object> model){
        model.put("musics", playerFacade.getDownloadedMusic(searchCommand.getGenreId(), searchCommand.getReq(),pageable));
        model.put("categories",categoryDao.getAll());
        return "account/redownload";
    }


    @RequestMapping(value = "/redownloadMusic", method = RequestMethod.GET)
    public @ResponseBody
    byte[] redownloadMusic(@RequestParam Long musicId,HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mediaFacade.getMedia(musicId,playerFacade.getCurrentPlayer(),response);
    }


    @RequestMapping(value = "/myaccount/changepwd", method = RequestMethod.GET)
    public String changePwd(@ModelAttribute("changePwdBean")ChangePwdBean changePwdBean,BindingResult bindingResult,Map<String, Object> model) throws Exception {
        model.put("changed",false);
        return "account/changePwd";
    }


    @RequestMapping(value = "/myaccount/changepwd", method = RequestMethod.POST)
    public String changePwdSubmit(@ModelAttribute("changePwdBean")ChangePwdBean changePwdBean,BindingResult bindingResult,Map<String, Object> model,HttpServletRequest request) throws Exception {
        validator.validate(changePwdBean,bindingResult);
        playerFacade.validateChangePwdBean(changePwdBean, bindingResult, request.getLocale());
        if(!bindingResult.hasErrors()){
            playerFacade.changePasswd(changePwdBean.getNewPassword());
            model.put("changed",true);
            return "account/changePwd";
        }else{
            model.put("changed",false);
            return "account/changePwd";
        }
    }






}
