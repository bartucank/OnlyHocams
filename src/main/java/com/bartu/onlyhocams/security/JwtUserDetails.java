package com.bartu.onlyhocams.security;

import com.bartu.onlyhocams.entity.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class JwtUserDetails implements UserDetails {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private BigDecimal credit;

    //Not implemented for now
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Not implemented for now
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Not implemented for now
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Not implemented for now
    @Override
    public boolean isEnabled() {
        return true;
    }

    public JwtUserDetails(Long id, String username, String password, Role role, String name, String email, BigDecimal credit) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.name()));
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities=authorityList;
        this.name=name;
        this.email=email;
        this.credit=credit;
    }


}
