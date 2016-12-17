package fr.k2i.adbeback.webapp.command;

import fr.k2i.adbeback.core.business.user.Address;
import fr.k2i.adbeback.core.business.user.Sex;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev on 23/03/15.
 */
@Data
public class EnrollCommand implements Serializable{
    private String username;
    private String password;
    private String email;
    private Date birthday;
    private Address address;
    private Sex sex;



}
