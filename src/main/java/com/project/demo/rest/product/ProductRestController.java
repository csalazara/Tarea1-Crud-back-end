package com.project.demo.rest.product;

import com.project.demo.logic.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class ProductRestController {

    @Autowired
    private ProductRepository ProductRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public List<Product> getAllProduct() {
        return ProductRepository.findAll();
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return ProductRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ProductRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(product.getName());
                    existingCategory.setDescription(product.getDescription());
                    existingCategory.setPrice(product.getPrice());
                    existingCategory.setStockQuantity(product.getStockQuantity());
                    existingCategory.setCategory(product.getCategory());
                    return ProductRepository.save(existingCategory);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return ProductRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        ProductRepository.deleteById(id);
    }

}
