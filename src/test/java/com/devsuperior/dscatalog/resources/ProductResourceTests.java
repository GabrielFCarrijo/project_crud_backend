package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.regex.MatchResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;
	@MockBean
    private ProductService productService;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> dtoPage;

    @BeforeEach
    void varibleInicialization() {

        productDTO = Factory.createProductDTO();
        dtoPage = new PageImpl<>(List.of(productDTO));
        when(productService.findAllPaged(ArgumentMatchers.any())).thenReturn(dtoPage);
    }

    @Test
    void findAllShouldReturnPage() throws Exception {

		mockMvc.perform(get("/products")).andExpect(status().isOk());
    }


}











