package fr.k2i.adbeback.webapp.bean.security;

import lombok.*;
import lombok.experimental.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:03
 * Goal:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileBean{
    private Integer id;
    private String name;
    private String description;
    private List<RoleBean> roles = new ArrayList<>();
}
