package com.sensedia.desafio.integrations.dto.request;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountAuthCredentialsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}
