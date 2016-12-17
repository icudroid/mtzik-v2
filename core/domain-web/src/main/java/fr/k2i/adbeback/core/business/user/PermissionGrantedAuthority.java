package fr.k2i.adbeback.core.business.user;

import fr.k2i.adbeback.core.business.user.security.Permission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 15/01/14
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
@Data
public class PermissionGrantedAuthority implements Serializable, GrantedAuthority {

    private String permission;

    public PermissionGrantedAuthority(){}
    public PermissionGrantedAuthority(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
