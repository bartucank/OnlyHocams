package com.bartu.onlyhocams.service.impl;
import com.bartu.onlyhocams.entity.User;
import com.bartu.onlyhocams.repository.UserRepository;
import com.bartu.onlyhocams.security.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new JwtUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole(),user.getName(),user.getEmail(),user.getCredit());
    }


}
