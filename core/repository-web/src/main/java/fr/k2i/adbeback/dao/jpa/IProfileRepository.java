package fr.k2i.adbeback.dao.jpa;


import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.core.business.user.security.Profile;
import fr.k2i.adbeback.dao.IGenericDao;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IProfileRepository extends IGenericDao<Profile,Integer> {

    String MTZIK_PROFILE = "MTZIK_PROFILE";
    String LABEL_PROFILE = "LABEL_PROFILE";
    String ARTIST_LABEL = "ARTIST_PROFILE";
    String ADMIN = "ADMIN_PROFILE";

    Set<GrantedAuthority> loadGrantedAuthorities(User user);

    Boolean existsByName(Integer id, String newName);

    List<Profile> findByIds(List<Integer> profileIds);

    Profile findByName(String name);
}
