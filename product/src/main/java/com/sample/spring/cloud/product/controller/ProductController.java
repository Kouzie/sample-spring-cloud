package com.sample.spring.cloud.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.product.model.Product;
import com.sample.spring.cloud.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ObjectMapper mapper;
    private final ProductService productService;

    @Value("${server.port}")
    String port;

    @PostMapping
    public Product add(@RequestBody Product product) throws JsonProcessingException {
        log.info("Products add : {}", mapper.writeValueAsString(product));
        return productService.add(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) throws JsonProcessingException {
        log.info("Products update : {}", mapper.writeValueAsString(product));
        return productService.update(product);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable("id") Long id) throws JsonProcessingException {
        Product product = productService.findById(id);
        log.info("Products findById : {}", mapper.writeValueAsString(product));
        return product;
    }

    @PostMapping("/{ids}")
    public List<Product> find(@PathVariable List<Long> ids) {
        System.out.println("find invoke, port:" + port);
        return productService.find(ids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Products delete: {}", id);
        productService.delete(id);
    }
}
