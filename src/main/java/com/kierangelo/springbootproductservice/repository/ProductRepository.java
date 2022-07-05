package com.kierangelo.springbootproductservice.repository;

import com.kierangelo.springbootproductservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByName(String name);
}
