package fr.k2i.adbeback.core.business.user;

import fr.k2i.adbeback.core.business.user.security.Profile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 15/01/14
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class BOUser implements UserDetails {
    private User user;
    Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     * @return true if account is still active
     */
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }


    public boolean isAccountExpired() {
        LocalDateTime now= LocalDateTime.now();
        return user.getExpirationDate()!=null && user.getExpirationDate().isBefore(now);
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     * @return false if account is locked
     */
    @Transient
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }


    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     * @return true if credentials haven't expired
     */
    @Transient
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired();
    }


    public boolean isCredentialsExpired(){
        LocalDateTime now= LocalDateTime.now();
        return user.getCredentialsExpirationDate()!=null && user.getCredentialsExpirationDate().isBefore(now);
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public BOUser(){}


    public BOUser(User user,Set<GrantedAuthority> grantedAuthorities){
        this.user = user;
        this.authorities = grantedAuthorities;
    }

}
