package com.example.demo;

import com.example.demo.dao.ProductServiceRequest;
import com.example.demo.dao.ProductServiceResponse;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductServiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductRepositoryTest {

    @LocalServerPort
    private int localPort;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private ProductServiceRepository repo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCheckProduct(){
        Product p = Product.builder()
                .name("iPhone")
                .description("iPhone 14 new model")
                .price(BigDecimal.valueOf(5000000))
                .build();

        repo.save(p);

        assertEquals(p.getName(),"iPhone");

    }

    @Test
    void product(){
        String url = "http://localhost:" + localPort + "/api/product"; // replace with your actual API endpoint

        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>() {};

        ResponseEntity<List<Product>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        List<Product> products = response.getBody();
        System.out.println("products = " + products);
//


    }


    @Test
    void testCreateProductMvc() throws Exception {
        ProductServiceRequest p = getProductRequest();
        String s = objectMapper.writeValueAsString(p);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)).andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    void testProductMvc() throws Exception {
        ProductServiceRequest p = getProductRequest();
        String s = objectMapper.writeValueAsString(p);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    private ProductServiceRequest getProductRequest(){
        return ProductServiceRequest.builder()
                .name("iPhone")
                .description("New iPhone")
                .price(BigDecimal.valueOf(500000))
                .build();
    }
}
