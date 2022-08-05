 
package com.example.demo.service;

import com.example.demo.dao.RoleDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService
{

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser (User user)
    {
        return userDao.save(user);
    }

    public void initRolesAndUser ()
    {
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        userRole.setRoleDescription("Default role for newly created user record");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        //        User user = new User();
        //        user.setUserFirstName("rishabh");
        //        user.setUserLastName("sinha");
        //        user.setUserName("ris123");
        //        user.setUserPassword(getEncodedPassword("ris@pass"));
        //        Set<Role> userRoles = new HashSet<>();
        //        userRoles.add(userRole);
        //        user.setRole(userRoles);
        //        userDao.save(user);

    }

    public String getEncodedPassword (String password)
    {
        return passwordEncoder.encode(password);
    }
}
