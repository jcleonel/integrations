package com.sensedia.desafio.integrations.service;

import com.sensedia.desafio.integrations.domain.Product;
import com.sensedia.desafio.integrations.dto.request.ProductRequestDTO;
import com.sensedia.desafio.integrations.dto.response.ProductResponseDTO;
import com.sensedia.desafio.integrations.exception.RequiredObjectIsNullException;
import com.sensedia.desafio.integrations.mocks.MockProduct;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    MockProduct input;

    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    ProductRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockProduct();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Product entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.getById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertEquals("Name Product1", result.getName());
        assertEquals("Description Product1", result.getDescription());
        assertEquals(BigDecimal.valueOf(25), result.getPrice());
        assertEquals(10, result.getStock());
    }

    @Test
    void testCreate() {
        ProductRequestDTO productRequestDTO = input.mockRequestDTO(1);

        Product persisted = new Product(1L, "Name Product1", "Description Product1", BigDecimal.valueOf(25), 10);
        persisted.setId(1L);

        doReturn(persisted).when(repository).save(any(Product.class));

        ProductResponseDTO result = service.create(productRequestDTO);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Name Product1", result.getName());
        assertEquals("Description Product1", result.getDescription());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateWithNullProduct() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Product entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.delete(1L);
    }

    @Test
    void testFindAll() {
        List<Product> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        List<ProductResponseDTO> productResponseDTOS = service.getAll();

        assertNotNull(productResponseDTOS);
        assertEquals(14, productResponseDTOS.size());

        var productOne = productResponseDTOS.get(1);

        assertNotNull(productOne);
        assertNotNull(productOne.getId());
        assertNotNull(productOne.getLinks());

        assertEquals("Name Product1", productOne.getName());
        assertEquals("Description Product1", productOne.getDescription());
        assertEquals(BigDecimal.valueOf(25), productOne.getPrice());
        assertEquals(10, productOne.getStock());

        var bookFour = productResponseDTOS.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assertEquals("Name Product4", bookFour.getName());
        assertEquals("Description Product4", bookFour.getDescription());
        assertEquals(BigDecimal.valueOf(25), bookFour.getPrice());
        assertEquals(10, bookFour.getStock());

        var bookSeven = productResponseDTOS.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assertEquals("Name Product7", bookSeven.getName());
        assertEquals("Description Product7", bookSeven.getDescription());
        assertEquals(BigDecimal.valueOf(25), bookSeven.getPrice());
        assertEquals(10, bookSeven.getStock());
    }

}
