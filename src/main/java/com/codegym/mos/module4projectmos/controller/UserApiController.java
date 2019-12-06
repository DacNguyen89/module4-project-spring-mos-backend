package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Role;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.model.form.SearchResponse;
import com.codegym.mos.module4projectmos.model.form.UserForm;
import com.codegym.mos.module4projectmos.model.util.CustomUserDetails;
import com.codegym.mos.module4projectmos.model.util.LoginRequest;
import com.codegym.mos.module4projectmos.model.util.LoginResponse;
import com.codegym.mos.module4projectmos.repository.RoleRepository;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.SongService;
import com.codegym.mos.module4projectmos.service.UserService;
import com.codegym.mos.module4projectmos.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("api")
public class UserApiController {
    private static final String DEFAULT_ROLE = "ROLE_USER";
    @Autowired
    FormConvertService formConvertService;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AvatarStorageService avatarStorageService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private SongService songService;

    @Autowired
    private ArtistService artistService;

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

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserForm userForm) {
        User user = formConvertService.convertToUser(userForm, true);
        if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Role role = roleRepository.findByName(DEFAULT_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUser() {
        User user = userDetailService.getCurrentUser();
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody User user) {
        User oldUser = userDetailService.getCurrentUser();
        if (user != null) {
            userService.setFieldsEdit(oldUser, user);
            userService.save(oldUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*@PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestPart("avatar") MultipartFile avatar, @RequestPart("id") String id) {
        Optional<User> user = userService.findById(Long.parseLong(id));
        if (user.isPresent()) {
            String fileName = avatarStorageService.storeFile(avatar, user.get());
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/avatar/")
                    .path(fileName)
                    .toUriString();
            user.get().setAvatarUrl(fileDownloadUri);
            userService.save(user.get());
            return new ResponseEntity<>("User's avatar uploaded successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("Not found user with the given id in database!", HttpStatus.NOT_FOUND);
    }*/

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestPart("avatar") MultipartFile avatar, @RequestPart("id") String id) {
        Optional<User> user = userService.findById(Long.parseLong(id));
        if (user.isPresent()) {
            String fileDownloadUri = avatarStorageService.saveToFirebaseStorage(user.get(), avatar);
            user.get().setAvatarUrl(fileDownloadUri);
            userService.save(user.get());
            return new ResponseEntity<>("User's avatar uploaded successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("Not found user with the given id in database!", HttpStatus.NOT_FOUND);
    }

    /*@PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestPart("user") User user , @RequestPart(value = "avatar",required = false) MultipartFile multipartFile, @RequestParam("id") Long id) {
        Optional<User> oldUser = userService.findById(id);
        if(oldUser.isPresent()) {
            if (multipartFile != null) {
                String fileDownloadUri = avatarStorageService.saveToFirebaseStorage(oldUser.get(),multipartFile);
                user.setAvatarUrl(fileDownloadUri);
            }
            userService.setFieldsEdit(oldUser.get(),user);
            userService.save(oldUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/

    @GetMapping("/avatar/{fileName:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("fileName") String fileName, HttpServletRequest request) {
        return downloadService.generateUrl(fileName, request, avatarStorageService);
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<SearchResponse> search(@RequestParam("name") String name) {
        SearchResponse searchResponse = userService.search(name);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }
}
