package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.user.Address;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: dimitri
 * Date: 09/12/14
 * Time: 12:04
 * Goal:
 */
@Data
public class RegisterBoBean implements Serializable{

    @NotNull
    private String name;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String phone;

    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;

    @NotNull
    private String country;
    @NotNull
    private String address;
    @NotNull
    private String zipCode;
    @NotNull
    private String city;

    @NotNull
    private String password;
    @NotNull
    private String rpassword;

    @NotNull
    private String birthDate;

    private boolean tnc;

    private TypeRegistration typeRegistration = TypeRegistration.ARTIST;



}
