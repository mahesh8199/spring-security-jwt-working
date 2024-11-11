package com.codelearner.service;

import com.codelearner.dto.Product;
import com.codelearner.entity.UserInfo;
import com.codelearner.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {


    private List<Product> products = null;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;


    @PostConstruct
    public void loadFromDb() {
        products = IntStream.rangeClosed(1, 50)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build()
                ).collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProductById(int id) {

        return products.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));

    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added";
    }

}
