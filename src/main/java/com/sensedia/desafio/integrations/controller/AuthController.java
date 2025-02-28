package com.sensedia.desafio.integrations.controller;

import com.sensedia.desafio.integrations.api.AuthApi;
import com.sensedia.desafio.integrations.dto.request.AccountAuthCredentialsDTO;
import com.sensedia.desafio.integrations.dto.response.TokenDTO;
import com.sensedia.desafio.integrations.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController implements AuthApi {

    @Autowired
    AuthService service;

    public ResponseEntity<TokenDTO> signin(AccountAuthCredentialsDTO credentials) {
        TokenDTO token = service.signIn(credentials);
        return ResponseEntity.ok().body(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        TokenDTO token = service.refreshToken(username, refreshToken);
        return ResponseEntity.ok().body(token);
    }

}
