//package com.sample.spring.cloud.product;
//
//
//import com.sample.spring.cloud.product.model.Product;
//import com.sample.spring.cloud.product.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ActiveProfiles("contract")
//public abstract class ProductServiceBase {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @MockBean
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void setup() {
//        RestAssuredMockMvc.webAppContextSetup(context);
//        final List<Product> products = new ArrayList<>();
//        products.add(Product.builder().id(1l).name("Test1").price(500).count(1).build());
//        products.add(Product.builder().id(2l).name("Test2").price(300).count(1).build());
//        products.add(Product.builder().id(3l).name("Test3").price(800).count(1).build());
//        products.add(Product.builder().id(4L).name("Test4").price(200).count(1).build());
//        products.add(Product.builder().id(5l).name("Test5").price(500).count(1).build());
//        products.add(Product.builder().id(6l).name("Test6").price(700).count(1).build());
//        when(productRepository.findAllById(anyList())).thenAnswer(new Answer<List<Product>>() {
//            @Override
//            public List<Product> answer(InvocationOnMock invocation) {
//                List<String> ids = invocation.getArgument(0, List.class);
//                return products.stream().filter(it -> ids.contains(it.getId())).collect(Collectors.toList());
//            }
//        });
//    }
//}
