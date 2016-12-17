package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.country.City;
import fr.k2i.adbeback.core.business.user.Sex;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 08/01/14
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
@Data
public class RegisterBean implements Serializable{
    private String speudo;
    private String password;
    private String passwordConfirm;
    private String email;
    private Sex sex;
    private City city;
}
