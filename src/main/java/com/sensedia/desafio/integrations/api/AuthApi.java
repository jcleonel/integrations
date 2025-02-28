package com.sensedia.desafio.integrations.api;

import com.sensedia.desafio.integrations.dto.request.AccountAuthCredentialsDTO;
import com.sensedia.desafio.integrations.dto.response.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public interface AuthApi {

    @PostMapping(value = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Authenticates a user and returns a token",
            description = "Validates user credentials and generates an access token for authentication.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TokenDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TokenDTO> signin(@RequestBody AccountAuthCredentialsDTO credentials);

    @PutMapping(value = "/refresh/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Refresh token for authenticated user and returns a token",
            description = "Generates a new access token using the provided refresh token and username.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TokenDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @Parameters({
            @Parameter(
                    name = "refreshToken",
                    description = "Refresh token with Bearer prefix. Example: Bearer your-refresh-token",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string")
            ),
            @Parameter(
                    name = "username",
                    description = "Username associated with the refresh token",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "string")
            )
    })
    ResponseEntity<TokenDTO> refreshToken(
            @PathVariable String username,
            @RequestHeader String refreshToken);

}