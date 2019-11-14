package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.model.form.UserForm;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FormConvertService {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User convertToUser(UserForm userForm, boolean createAction) {
        String username = userForm.getUsername();
        if (userService.findByUsername(username).isPresent() && createAction) return null;
        String password = passwordEncoder.encode(userForm.getPassword());
        String firstName = userForm.getFirstName();
        String lastName = userForm.getLastName();
        String phoneNumber = userForm.getPhoneNumber();
        Boolean gender = userForm.getGender();
        Date birthDate = userForm.getBirthDate();
        String email = userForm.getEmail();
        return new User(username, password, firstName, lastName, gender, birthDate, phoneNumber, email);
    }
}
