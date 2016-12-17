package fr.k2i.adbeback.webapp.bean.myaccount;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 19/01/14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ChangePwdBean implements Serializable{
    @NotEmpty
    private String oldPassword;

    @NotEmpty
    @Length(min = 6,max = 30)
    private String newPassword;

    @NotEmpty
    @Length(min = 6,max = 30)
    private String newConfirmPassword;
}
