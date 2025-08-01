package com.inventory.controller;



import com.inventory.dto.ProductDto; // Import your ProductDto
import com.inventory.model.Product;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Convert Product entity to ProductDto
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice()); // Directly set BigDecimal price
        dto.setStockQuantity(product.getStockQuantity());
        return dto;
    }

    // Convert ProductDto to Product entity (for creating/updating)
    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId()); // ID might be null for creation
        product.setName(dto.getName());
        product.setPrice(dto.getPrice()); // Directly set BigDecimal price
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    // --- API Endpoints ---

    // Create Product - ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product createdProduct = productService.createProduct(product); // Assuming productService.createProduct takes a Product entity
        return new ResponseEntity<>(convertToDto(createdProduct), HttpStatus.CREATED);
    }

    // Get All Products - Accessible by USER and ADMIN
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get Product by ID - Accessible by USER and ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(this::convertToDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update Product - ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        Product productDetails = convertToEntity(productDto);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return new ResponseEntity<>(convertToDto(updatedProduct), HttpStatus.OK);
    }

    // Delete Product - ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
