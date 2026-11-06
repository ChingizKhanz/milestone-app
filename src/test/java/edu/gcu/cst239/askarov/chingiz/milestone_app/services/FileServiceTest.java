package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

class FileServiceTest {

    @TempDir
    Path tempDir;

    private final FileService fileService = new FileService();
    private Path categoriesFile;
    private Path productsFile;

    @BeforeEach
    void writeTestFiles() throws IOException {
        categoriesFile = tempDir.resolve("categories.json");
        Files.writeString(categoriesFile, """
                [ { "category": "gpu", "description": "Graphics cards" },
                  { "category": "cpu", "description": "Desktop processors" } ]
                """);
        productsFile = tempDir.resolve("products.json");
        Files.writeString(productsFile, """
                [ { "id": 1, "name": "Graphics Card", "description": "Open box",
                    "dateOfManufacture": "2024-03-15", "price": 499.99, "category": "gpu" },
                  { "id": 2, "name": "Processor", "description": "New in box",
                    "dateOfManufacture": "2023-05-01", "price": 250.0, "category": "cpu" },
                  { "id": 3, "name": "Processor", "description": "Refurbished",
                    "dateOfManufacture": "2022-08-20", "price": 180.5, "category": "cpu" } ]
                """);
    }

    @Test
    void loadsControlledFileIntoProducts() throws IOException {
        // Act
        List<InventoryItem> items = fileService.loadInventory(categoriesFile, productsFile);

        // Assert
        assertEquals(3, items.size());
        Product first = items.get(0).getProduct();
        assertEquals("Graphics Card", first.getName());
        assertEquals(LocalDate.of(2024, 3, 15), first.getDateOfManufacture());
    }

    @Test
    void savedFileReadsBackWithSameValues() throws IOException {
        // Arrange
        List<InventoryItem> original = fileService.loadInventory(categoriesFile, productsFile);
        Path savedFile = tempDir.resolve("saved.json");

        // Act
        fileService.saveInventory(savedFile, original);
        List<InventoryItem> reloaded = fileService.loadInventory(categoriesFile, savedFile);

        // Assert
        assertEquals(original.size(), reloaded.size());
        for (int i = 0; i < original.size(); i++) {
            assertEquals(ProductMapper.toDto(original.get(i).getProduct()),
                    ProductMapper.toDto(reloaded.get(i).getProduct()));
        }
    }

    @Test
    void malformedJsonThrowsIOException() throws IOException {
        // Arrange
        Path badFile = tempDir.resolve("bad.json");
        Files.writeString(badFile, "[ { \"id\": 1, \"name\": ");

        // Act + Assert
        assertThrows(IOException.class, () -> fileService.loadInventory(categoriesFile, badFile));
    }

    @Test
    void unknownCategoryInFileStopsLoading() throws IOException {
        // Arrange
        Path badFile = tempDir.resolve("unknown.json");
        Files.writeString(badFile, """
                [ { "id": 1, "name": "Monitor", "description": "Open box",
                    "dateOfManufacture": "2024-03-15", "price": 199.99, "category": "monitor" } ]
                """);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> fileService.loadInventory(categoriesFile, badFile));
    }

    @Test
    void productionFileHas100Products() throws IOException {
        // Act
        List<InventoryItem> items = fileService.loadInventory(
                Path.of("src", "main", "resources", "data", "categories.json"),
                Path.of("src", "main", "resources", "data", "products.json"));
        // Assert
        assertEquals(100, items.size());
    }
}