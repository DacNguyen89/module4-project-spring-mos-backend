package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.User;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplWithSpringData extends UserService {
    @Override
    protected CrudRepository<User, Long> repository() {
        return null;
    }
}
