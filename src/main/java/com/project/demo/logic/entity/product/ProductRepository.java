package com.project.demo.logic.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT u FROM Product u WHERE LOWER(u.name) LIKE %?1%")
    List<Product> findProductsWithCharacterInName(String character);

    @Query("SELECT u FROM Product u WHERE u.name = ?1")
    Optional<Product> findByName(String name);

}
