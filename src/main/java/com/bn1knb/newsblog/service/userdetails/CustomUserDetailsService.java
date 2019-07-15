package com.bn1knb.newsblog.service.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface CustomUserDetailsService extends UserDetailsService {

    Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<String> roles);
}
