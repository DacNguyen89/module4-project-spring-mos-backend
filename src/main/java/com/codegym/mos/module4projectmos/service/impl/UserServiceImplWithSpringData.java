package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.User;
import com.codegym.mos.module4projectmos.repository.UserRepository;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplWithSpringData extends UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected CrudRepository<User, Long> repository() {
        return userRepository;
    }
}
