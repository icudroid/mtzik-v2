package fr.k2i.adbeback.webapp.bean.builder.user;

import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.webapp.bean.UserBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dev on 08/02/15.
 */
public class UserBeanBuilder {
    public static List<UserBean> build(List<User> users) {
        return users
                .stream()
                .map(UserBeanBuilder::build)
                .collect(Collectors.toList());
    }


    public static UserBean build(User user) {
        return UserBean.builder()
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .id(user.getId())
                .identity(IdentityBuilder.build(user.getIdentity()))
                .newsletter(user.getNewsletter())
                .profiles(ProfileBeanBuilder.build(user.getProfiles()))
                .username(user.getUsername())
                .build();
    }




}
