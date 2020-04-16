package pl.ostrowski.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.ostrowski.products.dto.ProductDto;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        createTestProducts();
    }

    @Test
    void whenPostNewProducts_returnAllOfThem() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].sku", is(1)))
                .andExpect(jsonPath("$[1].sku", is(2)))
                .andExpect(jsonPath("$[2].sku", is(3)));
    }

    @Test
    void whenPostNewProducts_deleteOneOfThem_returnRest() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sku", is(2)))
                .andExpect(jsonPath("$[1].sku", is(3)));

    }

    @Test
    void whenPostNewProducts_updateOneOfThem_thenValueWasChangedProperly() throws Exception {
        ProductDto updateProductDto = createProductDtoWithSku(2L);
        updateProductDto.setName("new test name");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writer().writeValueAsString(updateProductDto);

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("new test name")));
    }


    private void createTestProducts() throws Exception {
        for (int i = 0; i < 3; i++) {
            ProductDto productDto = createProductDtoWithSku(0L);
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writer().writeValueAsString(productDto);

            mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isOk());
        }
    }

    private ProductDto createProductDtoWithSku(long sku) {
        return new ProductDto(sku, "p1", BigDecimal.TEN);
    }
}