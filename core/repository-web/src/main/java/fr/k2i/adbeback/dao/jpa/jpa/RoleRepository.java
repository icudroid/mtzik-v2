package fr.k2i.adbeback.dao.jpa.jpa;


import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import fr.k2i.adbeback.core.business.user.security.QRole;
import fr.k2i.adbeback.core.business.user.security.Role;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class RoleRepository extends GenericDaoJpa<Role, Integer> implements IRoleRepository {
    @Override
    public List<Role> findAllInId(List<Integer> rolesId) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QRole role = QRole.role;
        query.from(role).where(role.id.in(rolesId));
        return query.list(role);
    }

    @Override
    public boolean existsByName(Integer id, String newName) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QRole role = QRole.role;


        BooleanExpression exp = role.name.equalsIgnoreCase(newName);
        if(id != null){
            exp = exp.and(role.id.ne(id));
        }

        query.from(role).where(exp);

        return query.exists();
    }



}
