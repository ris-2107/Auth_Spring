

package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername (String username)
        throws UsernameNotFoundException
    {
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getUserPassword(),
                getAuthority(user)
            );
        }
        else {
            throw new UsernameNotFoundException(
                "User not found with username: " + username);
        }
    }

    private Set getAuthority (User user)
    {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role ->
        {
            authorities.add(new SimpleGrantedAuthority(
                "ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

}