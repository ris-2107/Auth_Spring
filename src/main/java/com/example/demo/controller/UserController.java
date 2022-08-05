 
package com.example.demo.controller;

import com.example.demo.dao.RoleDao;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController
{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @PostConstruct
    public void initRolesAndUsers ()
    {
        userService.initRolesAndUser();
    }

    @PostMapping({ "/registerNewUser" })
    public User registerNewUser (
        @RequestBody
        User user)
    {
        Role role = roleDao.findById("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        user.setUserPassword(userService.getEncodedPassword(user.getUserPassword()));
        return userService.registerNewUser(user);
    }

    @GetMapping({ "/forAdmin" })
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmin ()
    {
        return "Hello from Admin, This Url is for Admin only";
    }

    @GetMapping({ "/forUser" })
    @PreAuthorize("hasRole('USER')")
    public String forUser ()
    {
        return "Hello from User, This Url is for User only";
    }

}
