package fr.k2i.adbeback.dao.jpa.jpa;


import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import fr.k2i.adbeback.core.business.user.PermissionGrantedAuthority;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.core.business.user.security.*;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IProfileRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class ProfileRepository extends GenericDaoJpa<Profile,Integer> implements IProfileRepository {

    @Override
    public Set<GrantedAuthority> loadGrantedAuthorities(User user) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QProfile profile = QProfile.profile;
        QRole role = QRole.role;
        QPermission permission = QPermission.permission;

        query.from(profile) .join(profile.roles, role)
                            .join(role.permissions,permission)
                .where(profile.in(user.getProfiles()));

        return  getAuthorities(query.list(permission));
    }

    private Set<GrantedAuthority> getAuthorities(List<Permission> list) {
        Set<GrantedAuthority> permissionsResult = new HashSet<>();

        for (Permission p_action : list) {
            StringBuilder perm = new StringBuilder();
            perm.append("ROLE_").append(p_action.getName());
            if(p_action.getRight()!=null){
                perm.append("_").append(p_action.getRight().getPersistentValue());
            }

                permissionsResult.add(new PermissionGrantedAuthority(perm.toString()));
        }
        return permissionsResult;
    }

    @Override
    public Boolean existsByName(Integer id,String newName) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QProfile profile = QProfile.profile;


        BooleanExpression exp = profile.name.equalsIgnoreCase(newName);
        if(id != null){
            exp = exp.and(profile.id.ne(id));
        }

        query.from(profile).where(exp);

        return query.exists();

    }

    @Override
    public List<Profile> findByIds(List<Integer> profileIds) {

        JPAQuery query = createQuery();

        QProfile profile = QProfile.profile;

        query.from(profile).where(profile.id.in(profileIds));

        return query.list(profile);
    }

    @Override
    public Profile findByName(String name) {
        JPAQuery query = createQuery();

        QProfile profile = QProfile.profile;

        query.from(profile).where(profile.name.eq(name));

        return query.uniqueResult(profile);
    }


}
