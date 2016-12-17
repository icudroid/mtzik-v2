package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import fr.k2i.adbeback.webapp.command.UserType;
import lombok.*;
import lombok.experimental.Builder;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductorBean  extends IdentityBean  {
    private Date lastRelease;

    public ProductorBean(Long id, String firstName, String lastName, String photo,LocalDate lastRelease) {
        super(id, firstName, lastName,photo);
        this.lastRelease = DateUtils.asDate(lastRelease);
        type = UserType.LABEL;
    }

    public ProductorBean(Long id, String firstName, String lastName,String photo) {
        super(id, firstName, lastName,photo);
        type = UserType.LABEL;
    }

    public ProductorBean(Productor productor) {
        this(productor.getId(),productor.getFirstName(),productor.getLastName(),productor.getPhoto(),productor.getBirthday());
    }
}
