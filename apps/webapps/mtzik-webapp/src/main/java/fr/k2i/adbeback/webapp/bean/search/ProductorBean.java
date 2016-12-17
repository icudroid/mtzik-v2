package fr.k2i.adbeback.webapp.bean.search;

import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ProductorBean extends PersonBean {
    private Date lastRelease;

    public ProductorBean(Long id, String firstName, String lastName, String photo,LocalDate lastRelease) {
        super(id, firstName, lastName,photo);
        this.lastRelease = DateUtils.asDate(lastRelease);
    }

    public ProductorBean(Long id, String firstName, String lastName,String photo) {
        super(id, firstName, lastName,photo);
    }
}
