package fr.k2i.adbeback.dao.jpa.jpa;


import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.user.security.Permission;
import fr.k2i.adbeback.core.business.user.security.QPermission;
import fr.k2i.adbeback.core.business.user.security.QRole;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IPermissionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class PermissionRepository extends GenericDaoJpa<Permission,Integer> implements IPermissionRepository {


    @Override
    public Map<String,List<Permission>> findPermissionsByRoleId(Integer roleId) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QRole role = QRole.role;
        QPermission permission = QPermission.permission;

        query.from(role)
                .join(role.permissions,permission)
                .groupBy(permission,permission.name)
                .where(role.id.eq(roleId));

        return createMapPermissions(query.list(permission.name, permission));
    }

    @Override
    public Map<String, List<Permission>> findAllPermissions() {
        JPAQuery query = new JPAQuery(getEntityManager());

        QPermission permission = QPermission.permission;

        query.from(permission)
                .groupBy(permission, permission.name);

        return createMapPermissions(query.list(permission.name, permission));
    }



    private Map<String, List<Permission>> createMapPermissions(List<Tuple> list) {
        Map<String,List<Permission>> permissionMap = new HashMap<>();

        for (Tuple tuple : list) {
            String permissionName = tuple.get(QPermission.permission.name);
            Permission permissionObj = tuple.get(QPermission.permission);
            List<Permission> permissions = permissionMap.get(permissionName);
            if(permissions==null){
                permissions = new ArrayList<>();
            }
            permissions.add(permissionObj);
            permissionMap.put(permissionName,permissions);
        }
        return permissionMap;
    }

}
