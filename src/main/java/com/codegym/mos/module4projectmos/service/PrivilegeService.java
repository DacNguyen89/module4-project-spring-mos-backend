package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Privilege;

public interface PrivilegeService {
    Privilege findByName(String name);

    void save(Privilege privilege);
}
