package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> dtoPage;
    private Long idExist;
    private Long idNotExist;

    @BeforeEach
    void varibleInicialization() {

        productDTO = Factory.createProductDTO();
        dtoPage = new PageImpl<>(List.of(productDTO));
        idExist = 1L;
        idNotExist = 100L;

        when(productService.findAllPaged(ArgumentMatchers.any())).thenReturn(dtoPage);

        when(productService.findById(idExist)).thenReturn(productDTO);
        when(productService.findById(idNotExist)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void findAllShouldReturnPage() throws Exception {

        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", idExist).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
    }

    @Test
    void findByIdShouldNotReturnProductAndReturnNotFound() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", idNotExist).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }
}











