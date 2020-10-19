package com.siaivo.shipments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siaivo.shipments.model.Role;
import com.siaivo.shipments.repository.RoleRepository;

import java.util.List;
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> listAllRoles(){   return roleRepository.findAll() ;   }
    //    @Override
//    public void saveRole(Role role) {
//        roleRepository.save(role);}
    @Override
    public Role findById(int id) {
        return roleRepository.findById(id);
    }
    @Override
    public Role findByRole(String role) {return roleRepository.findByRole(role);}
}
