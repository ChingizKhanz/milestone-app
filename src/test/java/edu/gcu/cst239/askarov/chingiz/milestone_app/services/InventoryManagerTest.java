package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

class InventoryManagerTest {

    private InventoryItem gpuItem() {
        Product product = Product.builder().id(1).name("GeForce RTX 4070").description("graphics card")
                .dateOfManufacture(LocalDate.of(2023, 4, 12)).price(550).build();
        return new InventoryItem(product, 5);
    }

    @Test
    void separateManagersDoNotShareInventory() {
        // Arrange
        InventoryManager first = new InventoryManager(List.of(gpuItem()));
        InventoryManager second = new InventoryManager(List.of(gpuItem()));

        // Act
        first.removeProductById(1);

        // Assert
        assertEquals(0, first.getAllInventoryItems().size());
        assertEquals(1, second.getAllInventoryItems().size());
    }
}