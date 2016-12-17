package fr.k2i.adbeback.webapp.controller;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.core.business.contact.ContactForm;
import fr.k2i.adbeback.core.business.contact.SubjectMessage;
import fr.k2i.adbeback.dao.IContactFormDao;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/01/14
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ContactFormController {


    @Autowired(required = false)
    Validator validator;

    @InitBinder("contactForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
    @Autowired
    private IContactFormDao contactFormDao;

    @ModelAttribute("subjects")
    public List<SubjectMessage> subjectMessages(){
        return Lists.newArrayList(SubjectMessage.values());
    }


    @RequestMapping(value = "/contactForm",method = RequestMethod.GET)
    public String showSignUp(@ModelAttribute("contactForm") ContactForm contactForm,Map<String, Object> model){
        model.put("reCaptcha",getRecaptchaHtml());
        model.put("captchaError",false);
        return "contactForm";
    }

    private String getRecaptchaHtml() {
        ReCaptcha reCaptcha = ReCaptchaFactory.newReCaptcha("6LelBe0SAAAAAHUFDrYui1-MTBKBP_vHC-LBUzm1", "6LelBe0SAAAAAFG7dA63XRDi8uUuJKcHNxHGapLv", false);
        return reCaptcha.createRecaptchaHtml(null, null);
    }

    @RequestMapping(value = "/contactForm",method = RequestMethod.POST)
    public String submitSignUp(@Valid @ModelAttribute("contactForm") ContactForm contactForm,BindingResult bindingResult,Map<String, Object> model,HttpServletRequest request){
        model.put("reCaptcha",getRecaptchaHtml());

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        String remoteAddr = request.getRemoteAddr();

        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LelBe0SAAAAAFG7dA63XRDi8uUuJKcHNxHGapLv");

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        boolean reCaptchaResponseValid = reCaptchaResponse.isValid();
        model.put("captchaError",!reCaptchaResponseValid);
        if (reCaptchaResponseValid && !bindingResult.hasErrors()) {
            contactFormDao.save(contactForm);
            model.put("sended",true);
            contactForm.setMessage(null);
            contactForm.setId(null);
            return "contactForm";
        }else{
            return "contactForm";
        }
    }




}
