package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Role;

public interface RoleService {
    Role findByName(String name);

    void save(Role role);
}
