package com.example.demo.service;

import com.example.demo.dao.ProductServiceResponse;
import com.example.demo.model.Product;
import com.example.demo.dao.ProductServiceRequest;
import com.example.demo.repository.ProductServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final ProductServiceRepository repo;

    public void createProduct(ProductServiceRequest request) {
        Product p = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice()
                ).build();

        repo.save(p);
        log.info("Product {} is saved", p.getId());
    }

    public List<ProductServiceResponse> getAllProducts(){
        List<Product> lst = repo.findAll();
        return lst.stream()
                .map(p -> {
           ProductServiceResponse r = new ProductServiceResponse();
           r.setId(p.getId());
           r.setName(p.getName());
           r.setDescription(p.getDescription());
           r.setPrice(p.getPrice());
           return r;
        }).collect(Collectors.toList());
    }
}
