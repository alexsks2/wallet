package com.solbeg.wallet.controller;

import com.solbeg.wallet.requests.AuthRequest;
import com.solbeg.wallet.security.JwtTokenService;
import com.solbeg.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String getToken(@RequestBody AuthRequest request) {

        var authentication = new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword());
        authenticationManager.authenticate(authentication);

        UserDetails userDetails = userService.loadUserByUsername(request.getLogin());
        return tokenService.generateToken(userDetails);
    }
}
