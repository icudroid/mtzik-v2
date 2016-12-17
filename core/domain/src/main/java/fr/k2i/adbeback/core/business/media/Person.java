package fr.k2i.adbeback.core.business.media;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by dev on 31/01/15.
 */
@Data
@Entity
@DiscriminatorValue("Person")
public class Person extends Identity {
    @Override
    public String getFullName() {
        return firstName+((!StringUtils.isEmpty(firstName))?" ":"")+lastName;
    }

}
