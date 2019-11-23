package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Privilege;
import com.codegym.mos.module4projectmos.repository.PrivilegeRepository;
import com.codegym.mos.module4projectmos.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    @Autowired
    PrivilegeRepository privilegeRepository;

    @Override
    public Privilege findByName(String name) {
        return privilegeRepository.findByName(name);
    }

    @Override
    public void save(Privilege privilege) {
        privilegeRepository.save(privilege);
    }

}
