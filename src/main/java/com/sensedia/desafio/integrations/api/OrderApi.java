package com.sensedia.desafio.integrations.api;

import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/orders")
@Tag(name = "Order")
public interface OrderApi {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Order", description = "Create Order",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO orderRequest);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all Orders", description = "Finds all Orders",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Page<OrderResponseDTO>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "25") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds a Order", description = "Finds a Order",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id);

    @PutMapping(value = "/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a Order Status",
            description = "Updates a Order Status",
            responses = {
                    @ApiResponse(description = "Updated a Order Status", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequestDTO statusRequest);

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cancel a Order",
            description = "Cancell a Order",
            responses = {
                    @ApiResponse(description = "Cancel a Order", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id);
}
