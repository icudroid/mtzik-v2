package fr.k2i.adbeback.webapp.bean.builder.security;


import fr.k2i.adbeback.core.business.user.security.Permission;
import fr.k2i.adbeback.webapp.bean.security.PermissionBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 14:11
 * Goal:
 */
public class PermissionBeanBuilder {


    public static List<PermissionBean> build(List<Permission> permissions){
        return  permissions
                    .stream()
                    .map(PermissionBeanBuilder::build)
                    .collect(Collectors.toList());
    }

    public static PermissionBean build(Permission permission){
        return
                PermissionBean.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .right((permission.getRight() != null) ? permission.getRight().name() : null)
                        .description(permission.getDescription())
                        .build();
    }

}
