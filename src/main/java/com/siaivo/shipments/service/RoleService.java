package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(int id) ;
    List<Role> listAllRoles();
    Role findByRole(String role);
}