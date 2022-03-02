package com.conghuhu.auth;

import com.conghuhu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @author conghuhu
 * @create 2021-09-29 16:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetail implements UserDetails {

    private User user;
    private String password;
    private String username;
    /**
     * 用户权限列表
     */
    private Set<GrantedAuthority> authorities;
    /**
     * 账号是否过期
     */
    private boolean accountNonExpired = true;
    /**
     * 账号是否锁定
     */
    private boolean accountNonLocked = true;
    /**
     * 密码是否过期
     */
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
