package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.util.CustomUserDetails;
import com.codegym.mos.module4projectmos.model.util.LoginRequest;
import com.codegym.mos.module4projectmos.model.util.LoginResponse;
import com.codegym.mos.module4projectmos.service.UserService;
import com.codegym.mos.module4projectmos.service.impl.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("api")
public class UserApiController {
    private static final String DEFAULT_ROLE = "ROLE_USER";
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /*@GetMapping(value = "/api/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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
    }*/

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        LoginResponse loginResponse = new LoginResponse(jwt);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
