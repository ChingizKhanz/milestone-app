package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeParseException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductDto;

class ProductValidationTest {

    private final Set<String> categories = Set.of("gpu", "cpu", "ssd", "ram", "motherboard");

    @Test
    void knownCategoryIsAccepted() {
        // Arrange
        ProductDto dto = new ProductDto(1, "Processor", "New in box", "2023-05-01", 250.00, "cpu");

        // Act + Assert
        assertDoesNotThrow(() -> new FileService().validate(dto, categories));
    }

    @Test
    void unknownCategoryIsRejected() {
        // Arrange
        ProductDto dto = new ProductDto(1, "Monitor", "New in box", "2023-05-01", 250.00, "monitor");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new FileService().validate(dto, categories));
    }

    @Test
    void badPriceIsRejected() {
        // Arrange
        ProductDto dto = new ProductDto(1, "Processor", "New in box", "2023-05-01", 0, "cpu");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new FileService().validate(dto, categories));
    }

    @Test
    void badDateIsRejected() {
        // Arrange
        ProductDto dto = new ProductDto(1, "Processor", "New in box", "05/01/2023", 250.00, "cpu");

        // Act + Assert
        assertThrows(DateTimeParseException.class, () -> new FileService().validate(dto, categories));
    }
}