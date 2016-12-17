package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.core.business.user.security.Permission;
import fr.k2i.adbeback.core.business.user.security.Profile;
import fr.k2i.adbeback.dao.jpa.IPermissionRepository;
import fr.k2i.adbeback.dao.jpa.IProfileRepository;
import fr.k2i.adbeback.dao.jpa.IRoleRepository;
import fr.k2i.adbeback.dao.jpa.IUserRepository;
import fr.k2i.adbeback.logger.Log;
import fr.k2i.adbeback.webapp.bean.builder.security.ProfileBeanBuilder;
import fr.k2i.adbeback.webapp.bean.builder.security.RoleBeanBuilder;
import fr.k2i.adbeback.webapp.bean.dismantler.ProfileDismantler;
import fr.k2i.adbeback.webapp.bean.dismantler.RoleDismantler;
import fr.k2i.adbeback.webapp.bean.security.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:50
 * Goal:
 */
@Service
public class PermissionFacade {

    @Log
    private Logger logger;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IRoleRepository roleRepository;


    @Autowired
    private IPermissionRepository permissionRepository;


    @Autowired
    private IUserRepository userRepository;



    @Transactional
    public List<ProfileBean> findAllProfiles() {
        return ProfileBeanBuilder.build(profileRepository.getAll());
    }

    @Transactional
    public ProfileBean getProfile(Integer id) {
        return ProfileBeanBuilder.build(profileRepository.get(id));
    }


    public List<PermissionRightDto> findAllPermissions() {
        List<PermissionRightDto> res = new ArrayList<>();
        Map<String,List<Permission>> permissions = permissionRepository.findAllPermissions();


        for (Map.Entry<String, List<Permission>> entry : permissions.entrySet()) {
            PermissionRightDto.PermissionRightDtoBuilder builder = PermissionRightDto.builder();
            List<RightDto> rights = entry.getValue().stream().map(permission -> RightDto.builder()
                    .id(permission.getId())
                    .right(permission.getRight())
                    .description(permission.getDescription())
                    .build()).collect(Collectors.toList());

            builder.permission(entry.getKey()).rights(rights);
            res.add(builder.build());

        }

        return res;

    }

    @Transactional
    public List<PermissionRightDto> findPermissionsByRoleId(Integer roleId) {
        Map<String,List<Permission>> permissions = permissionRepository.findPermissionsByRoleId(roleId);
        return getPermissionRightDtos(permissions);
    }


    public Boolean existProfile(Integer id, String newName) {
        return profileRepository.existsByName(id,newName);
    }


    public Boolean existRole(Integer id, String newName) {
        return roleRepository.existsByName(id,newName);
    }

    @Transactional
    public void saveProfile(ProfileBean profileBean){
        Profile profile = ProfileDismantler.buildProfile(profileBean);
        profileRepository.save(profile);
    }


    public List<RoleBean> getAllRole() {
        return RoleBeanBuilder.build(roleRepository.getAll());
    }


    @Transactional
    public void deleteProfile(Integer idProfile) {
        Profile profile = profileRepository.get(idProfile);
        List<User> users =userRepository.findUsersWithProfile(idProfile);
        for (User user : users) {
            user.getProfiles().remove(profile);
        }
        profileRepository.remove(profile);
    }

    @Transactional
    public void createProfile(ProfileBean profile) {
        List<RoleBean> roles = profile.getRoles()
                                            .stream()
                                            .filter(roleBean -> roleBean.getId() != null)
                                            //.map(roleBean -> roleBean.getId())
                                            .collect(Collectors.toList());

        profile.setRoles(roles);
        profileRepository.save(ProfileDismantler.buildProfile(profile));

    }

    @Transactional
    public void deleteRoleById(Integer id) {
        roleRepository.remove(id);
    }


    @Transactional
    public RoleBean getRole(Integer id) {
        return RoleBeanBuilder.build(roleRepository.get(id));
    }

    @Transactional
    public List<PermissionRightDto> getAllPermissions() {
        Map<String,List<Permission>> permissions = permissionRepository.findAllPermissions();
        return getPermissionRightDtos(permissions);
    }

    private List<PermissionRightDto> getPermissionRightDtos(Map<String, List<Permission>> permissions) {
        List<PermissionRightDto> res = new ArrayList<>();
        for (Map.Entry<String, List<Permission>> entry : permissions.entrySet()) {
            PermissionRightDto.PermissionRightDtoBuilder builder = PermissionRightDto.builder();
            List<RightDto> rights = entry.getValue().stream().map(permission -> RightDto.builder()
                    .id(permission.getId())
                    .right(permission.getRight())
                    .description(permission.getDescription())
                    .build()).collect(Collectors.toList());

            builder.permission(entry.getKey()).rights(rights);
            res.add(builder.build());

        }
        return res;
    }

    @Transactional
    public void saveRole(RoleBean role) {
        List<PermissionBean> permissions = role.getPermissions()
                .stream()
                .filter(roleBean -> roleBean.getId() != null)
                .collect(Collectors.toList());

        role.setPermissions(permissions);
        roleRepository.save(RoleDismantler.buildRole(role));
    }
}
