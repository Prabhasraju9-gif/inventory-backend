package com.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

// For product requests and responses
@Data
public class ProductDto {
    private Long id; // Read-only, for responses
    @NotBlank(message = "Product name is required")
    private String name;
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    @NotNull(message = "stock quantity cannot be null")
    @Min(value = 0,message = "stock quantity cannot be negative")
    private Integer stockQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Product name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Product name is required") String name) {
        this.name = name;
    }

    public @Positive(message = "Price must be positive") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@Positive(message = "Price must be positive") BigDecimal price) {
        this.price = price;
    }

    public @NotNull(message = "stock quantity cannot be null") @Min(value = 0, message = "stock quantity cannot be negative") Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(@NotNull(message = "stock quantity cannot be null") @Min(value = 0, message = "stock quantity cannot be negative") Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
