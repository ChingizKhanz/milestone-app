package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductDto;

class ProductMapperTest {

    private ProductDto gpuDto() {
        return new ProductDto(7, "Graphics Card", "Open box", "2024-03-15", 499.99, "gpu");
    }

    @Test
    void dtoMapsToProduct() {
        // Arrange
        ProductDto dto = gpuDto();

        // Act
        Product product = ProductMapper.toProduct(dto);

        // Assert
        assertEquals(7, product.getId());
        assertEquals("Graphics Card", product.getName());
        assertEquals("Open box", product.getDescription());
        assertEquals(499.99, product.getPrice());
        assertEquals("gpu", product.getCategory());
    }

    @Test
    void dateTextBecomesLocalDate() {
        // Arrange
        ProductDto dto = gpuDto();

        // Act
        Product product = ProductMapper.toProduct(dto);

        // Assert
        assertEquals(LocalDate.of(2024, 3, 15), product.getDateOfManufacture());
    }

    @Test
    void productMapsBackToSameDto() {
        // Arrange
        ProductDto dto = gpuDto();

        // Act
        ProductDto back = ProductMapper.toDto(ProductMapper.toProduct(dto));

        // Assert
        assertEquals(dto, back);
    }
}