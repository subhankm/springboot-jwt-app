package com.example.demo.service;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import com.example.demo.repo.TokenRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.securityconfig.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public String register(User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = repository.save(user);

        return "User registration was successful";
    }

    public String authenticate(User request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        revokeAllTokenByUser(user);
        saveUserToken(accessToken,user);
        return accessToken;

    }

    private void saveUserToken(String accessToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setLoggedOut("false");
        token.setUser(user);
        tokenRepository.save(token);

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t->t.setLoggedOut("true"));

        tokenRepository.saveAll(validTokens);
    }
}
