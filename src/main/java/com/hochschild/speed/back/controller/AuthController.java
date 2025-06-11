package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.auth.User;
import com.hochschild.speed.back.model.auth.UserAuth;
import com.hochschild.speed.back.model.auth.UserAuthExternal;
import com.hochschild.speed.back.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody UserAuth userAuth) throws Exception {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        User user = authService.login(userAuth.getUsername(), userAuth.getPassword(), userAuth.getIdPerfil());
        if (user != null) {
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(user, httpStatus);
    }

    @PostMapping("/external")
    public ResponseEntity<User> loginExternal(@RequestBody UserAuthExternal userAuthExternal) throws Exception {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        User user = authService.loginExternal(userAuthExternal.getKey());
        if (user != null) {
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(user, httpStatus);
    }
}