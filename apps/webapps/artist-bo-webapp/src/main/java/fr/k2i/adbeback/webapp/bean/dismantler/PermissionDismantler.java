package fr.k2i.adbeback.webapp.bean.dismantler;


import fr.k2i.adbeback.core.business.user.security.Permission;
import fr.k2i.adbeback.core.business.user.security.Right;
import fr.k2i.adbeback.webapp.bean.security.PermissionBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:05
 * Goal:
 */
public class PermissionDismantler {
    public static List<Permission> buildPermissions(List<PermissionBean> permissionBeans) {
        return permissionBeans
                .stream()
                .map(PermissionDismantler::buildPermission)
                .collect(Collectors.toList());
    }


    public static Permission buildPermission(PermissionBean permission) {
        return  Permission.builder()
                .id(permission.getId())
                .name(permission.getName())
                .right((permission.getRight() != null) ? Right.valueOf(permission.getRight()) : null)
                .description(permission.getDescription())
                .build();
    }
}
