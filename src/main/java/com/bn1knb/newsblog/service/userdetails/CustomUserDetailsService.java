package com.bn1knb.newsblog.service.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public interface CustomUserDetailsService extends UserDetailsService {

    Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<String> roles);
}
