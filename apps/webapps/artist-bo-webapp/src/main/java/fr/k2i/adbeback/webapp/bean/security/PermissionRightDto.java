package fr.k2i.adbeback.webapp.bean.security;

import lombok.Data;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * User: dimitri
 * Date: 13/01/15
 * Time: 14:35
 * Goal:
 */
@Data
@Builder
public class PermissionRightDto implements Serializable{
    private String permission;
    private List<RightDto> rights;
}
