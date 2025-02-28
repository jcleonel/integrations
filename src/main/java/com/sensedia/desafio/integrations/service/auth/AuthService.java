package com.sensedia.desafio.integrations.service.auth;

import com.sensedia.desafio.integrations.domain.User;
import com.sensedia.desafio.integrations.dto.request.AccountAuthCredentialsDTO;
import com.sensedia.desafio.integrations.dto.response.TokenDTO;
import com.sensedia.desafio.integrations.exception.InvalidClientException;
import com.sensedia.desafio.integrations.repository.UserRepository;
import com.sensedia.desafio.integrations.securiy.jwt.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public TokenDTO signIn(AccountAuthCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            throw new InvalidClientException("Invalid client request!");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword()
            )
        );

        User user = repository.findByUsername(credentials.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
        }

        TokenDTO token = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );

        if (token == null) {
            throw new InvalidClientException("Invalid client request!");
        }

        return token;
    }

    public TokenDTO refreshToken(String username, String refreshToken) {

        if (parametersAreInvalid(username, refreshToken)) {
            throw new InvalidClientException("Invalid client request!");
        }

        User user = repository.findByUsername(username);

        TokenDTO token;

        if (user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        if (token == null) {
            throw new InvalidClientException("Invalid client request!");
        }

        return token;
    }

    private static boolean credentialsIsInvalid(AccountAuthCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.getPassword()) ||
                StringUtils.isBlank(credentials.getUsername());
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

}