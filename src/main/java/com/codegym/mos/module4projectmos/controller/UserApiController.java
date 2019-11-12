package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.User;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin("*")
public class UserApiController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> getNational(@PathVariable Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping(value = "api/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> apiRegister(@Validated @RequestBody User user,
                                            BindingResult bindingResult,
                                            UriComponentsBuilder ucBuilder) {
        if (bindingResult.hasGlobalErrors() || bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(headers, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Iterable<User>> apiList() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/api/edit-profile/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateNational(@PathVariable("id") Long id,
                                               @RequestBody User user) {
        User originUser = userService.findOne(id);

        if (originUser == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        originUser.setUsername(user.getUsername());
        originUser.setPassword(user.getPassword());
        userService.save(originUser);
        return new ResponseEntity<User>(originUser, HttpStatus.OK);
    }

}
