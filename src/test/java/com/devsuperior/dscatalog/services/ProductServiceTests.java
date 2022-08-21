package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long notExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    private

    @BeforeEach
    void variableInitialization() {

        existingId = 1l;
        notExistingId = 1000l;
        dependentId = 4l;
        product = Factory.createProduct();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(notExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
        Mockito.when(productRepository.getOne(notExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(notExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(notExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void shouldReturnProductDTOWhenUpdateExists() throws Exception{

        ProductDTO product = Factory.createProductDTO();
        ProductDTO productDTO = productService.update(existingId, product);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void shouldReturnExceptionProductDTOWhenUpdateNotExists() throws Exception{

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            ProductDTO product = Factory.createProductDTO();
            productService.update(notExistingId, product);
        });
    }

    @Test
    public void shouldReturnProductDTOWhenIdExists() throws Exception{

        ProductDTO productDTO = productService.findById(existingId);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void shouldReturnExceptionProductDTOWhenIdNotExists() throws Exception{

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
           productService.findById(notExistingId);
        });
    }


    @Test
    public  void giveFindAllPagedSholdReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAll(pageable);
    }

    @Test
    public void givenDeleteShouldThrowDataIntegrityViolationExceptionWhenIdNotExists() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });

        Mockito.verify(productRepository).deleteById(dependentId);
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
