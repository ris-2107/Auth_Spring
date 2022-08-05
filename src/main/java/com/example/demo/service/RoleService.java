 
package com.example.demo.service;

import com.example.demo.dao.RoleDao;
import com.example.demo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    @Autowired
    private RoleDao roleDao;

    public Role createNewRole (Role role)
    {
        return roleDao.save(role);
    }
}
