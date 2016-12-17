package fr.k2i.adbeback.webapp.bean.security;

import fr.k2i.adbeback.core.business.user.security.Right;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

import java.io.Serializable;

/**
 * User: dimitri
 * Date: 13/01/15
 * Time: 15:11
 * Goal:
 */
@Getter
@Setter
@Builder
public class RightDto implements Serializable{
    private Integer id;
    private Boolean selected;
    private Right right;
    private String description;
}
