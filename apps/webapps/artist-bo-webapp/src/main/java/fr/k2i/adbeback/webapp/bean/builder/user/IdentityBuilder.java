package fr.k2i.adbeback.webapp.bean.builder.user;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Identity;
import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.core.business.user.AdminIdentity;
import fr.k2i.adbeback.core.business.user.security.Profile;
import fr.k2i.adbeback.webapp.bean.*;

/**
 * Created by dev on 08/02/15.
 */
public class IdentityBuilder {

    public static IdentityBean build(Identity identity){

        if (identity instanceof AdminIdentity) {
            AdminIdentity adminIdentity = (AdminIdentity) identity;
            return build(adminIdentity);
        }

        if (identity instanceof Productor) {
            Productor productor = (Productor) identity;
            return build(productor);
        }

        if (identity instanceof Person) {
            Person person = (Person) identity;
            return build(person);
        }

        if (identity instanceof Artist) {
            Artist artist = (Artist) identity;
            return build(artist);
        }

        return null;
    }


    private static IdentityBean build(AdminIdentity admin){
        return new AdminIdentityBean(admin);
    }

    private static IdentityBean build(Artist artist){
        return new ArtistBean(artist);
    }

    private static IdentityBean build(Person person){
        return new PersonBean(person);
    }

    private static IdentityBean build(Productor productor){
        return new ProductorBean(productor);
    }


}
