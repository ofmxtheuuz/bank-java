package dev.mxtheuz.bank.controller;

import dev.mxtheuz.bank.application.services.AuthService;
import dev.mxtheuz.bank.application.services.JwtService;
import dev.mxtheuz.bank.domain.dto.CreateUserDto;
import dev.mxtheuz.bank.domain.dto.HttpResponse;
import dev.mxtheuz.bank.domain.dto.LoginUserDto;
import dev.mxtheuz.bank.domain.dto.UserDto;
import dev.mxtheuz.bank.domain.entities.User;
import dev.mxtheuz.bank.domain.repositories.IUserRepository;
import dev.mxtheuz.bank.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    private final IUserRepository userRepository;

    private final Converter converter;

    @Autowired
    public AuthController(AuthService authService, JwtService jwtService, IUserRepository userRepository, Converter converter) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> Login(@RequestBody LoginUserDto dto) {
        if(authService.login(dto)) {
            User user = userRepository.findByEmail(dto.email());
            String token = jwtService.createToken(user.getId());
            return ResponseEntity.ok(new HttpResponse(200, "authorized", token));
        }

        return ResponseEntity.status(401).body(HttpResponse.builder()
                        .code(401)
                        .message("unauthorized")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> Register(@RequestBody CreateUserDto dto) {
        if(userRepository.findByEmail(dto.email()) == null) {
            UserDto user = converter.convertUserToUserDto(authService.register(dto));
            return ResponseEntity.status(201).body(new HttpResponse(201, "created", user));
        }

        return ResponseEntity.status(400).body(HttpResponse.builder()
                        .code(400)
                        .message("email_already_exists")
                .build());
    }
}
