package com.mjvs.jgsp.service;


import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary // Intellij pronalazi vise implementacija za UserDetailService,
        // zato koristimo ovu anotaciju, da bi ova implementacija imala prednost u odnosu na druge,
        // i da bi @Autowired znao koju implementaciju da izabere
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
    
        if(user == null) {
            String message = String.format("No user found with username '%s' in database.", username);
            logger.error(message);
        	throw new UsernameNotFoundException(message);
        }

        List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(user.getUserType().toString());
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), garantedAuthorities);
    }

}
