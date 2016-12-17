package fr.k2i.adbeback.webapp.bean.builder.user;

import fr.k2i.adbeback.core.business.user.security.Profile;
import fr.k2i.adbeback.webapp.bean.ProfileBean;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dev on 08/02/15.
 */
public class ProfileBeanBuilder {
    public static Set<ProfileBean> build(Set<Profile> profiles){
        return profiles
                .stream()
                .map(ProfileBeanBuilder::build)
                .collect(Collectors.toSet());
    }


    public static  ProfileBean build(Profile profile){
        return
                ProfileBean.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .description(profile.getDescription())
                        .build();
    }
}
