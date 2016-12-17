package fr.k2i.adbeback.webapp.command;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Identity;
import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.core.business.user.AdminIdentity;
import lombok.Getter;

/**
 * Created by dev on 08/02/15.
 */
@Getter
public enum UserType {
    MTZIK(Person.class),LABEL(Productor.class),ARTIST(Artist.class),ADMIN(AdminIdentity.class);

    private  Class<? extends Identity> type;
    UserType(Class<? extends Identity> id) {
        this.type = id;
    }
}
