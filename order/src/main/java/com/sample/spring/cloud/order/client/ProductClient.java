package com.sample.spring.cloud.order.client;

import com.sample.spring.cloud.order.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    @PostMapping("/{ids}")
    List<Product> findByIds(@PathVariable List<Long> ids);
}