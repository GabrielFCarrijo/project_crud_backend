package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  private long existingId;
  private long notExistingId;

  @BeforeEach
  void variableInitialization() {
    existingId = 1l;
    notExistingId = 1000l;
    Mockito.doNothing().when(productRepository).deleteById(existingId);
    Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository)
        .deleteById(notExistingId);
  }

  @Test
  public void givenDeleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      productService.delete(notExistingId);
    });

    Mockito.verify(productRepository).deleteById(notExistingId);
  }

  @Test
  public void givenDeleteShouldDoNothingWhenIdExists() {

    Assertions.assertDoesNotThrow(() -> {
      productService.delete(existingId);
    });

    Mockito.verify(productRepository).deleteById(existingId);
  }


}
