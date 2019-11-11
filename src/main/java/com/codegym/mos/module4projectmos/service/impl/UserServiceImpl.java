package com.codegym.mos.module4projectmos.service.impl;
import com.codegym.mos.module4projectmos.model.User;
import com.codegym.mos.module4projectmos.repository.UserRepository;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
