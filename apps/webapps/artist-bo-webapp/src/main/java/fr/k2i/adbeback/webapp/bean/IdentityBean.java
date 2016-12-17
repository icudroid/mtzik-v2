package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.webapp.command.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;
import org.springframework.util.StringUtils;

/**
 * Created by dev on 08/02/15.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class IdentityBean {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String photo;

    protected UserType type;

    public String getFullName(){
        if(StringUtils.isEmpty(firstName)){
            return lastName;
        }else{
            return firstName + " " + lastName;
        }

    }

    public IdentityBean(Long id, String firstName, String lastName, String photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
    }

}
