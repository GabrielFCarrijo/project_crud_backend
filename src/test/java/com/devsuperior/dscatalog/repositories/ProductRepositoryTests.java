package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
public class ProductRepositoryTests {

  @Autowired
  private ProductRepository productRepository;
  private Long idExist;
  private Long notExistingId;
  private Product product;

  @BeforeEach
  void variableInitialization() {
    idExist = 1L;
    notExistingId = 100L;
    product = Factory.createProduct();
  }

  @Test
  public void givenDeleteByIdShouldDeleteObjectIfIdExists() {

    productRepository.deleteById(idExist);
    Optional<Product> result = productRepository.findById(idExist);

    Assertions.assertFalse(result.isPresent());
  }

  @Test
  public void givenFindByIdShouldReturnNonEmptyOptionalWhenIdExist() {

    Optional<Product> result = productRepository.findById(idExist);
    Assertions.assertNotNull(result);
  }

  @Test
  public void givenFindByIdShouldReturnEmptyOptionalWhenIdNotExist() {

    Optional<Product> result = productRepository.findById(notExistingId);
    Assertions.assertTrue(result.isEmpty());
  }


  @Test
  public void givenDeleteShouldEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
      productRepository.deleteById(notExistingId);
    });
  }

  @Test
  public void givenSaveMustPersistInAutoIncrementWhenIdIsNull(){

    product.setId(null);
    product =  productRepository.save(product);

    Assertions.assertNotNull(product.getId());
    Assertions.assertEquals(26, product.getId());
  }
}
