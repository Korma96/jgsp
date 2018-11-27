package com.mjvs.jgsp.service;


import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
    
        if(user == null) {
        	throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(user.getUserType().toString());
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), garantedAuthorities);
    }

}
