package fr.k2i.adbeback.webapp.bean;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/01/14
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ChangePasswordBean implements Serializable{
    @NotEmpty
    private String password;

}
