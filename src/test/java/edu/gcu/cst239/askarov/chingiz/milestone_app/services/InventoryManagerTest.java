package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

class InventoryManagerTest {

    private InventoryItem gpuItem() {
        Product product = Product.builder().id(1).name("GeForce RTX 4070").description("graphics card")
                .dateOfManufacture(LocalDate.of(2023, 4, 12)).price(550).build();
        return new InventoryItem(product, 5);
    }

    private InventoryItem cpuItem() {
        Product product = Product.builder().id(2).name("Ryzen 9 7900X").description("12-core desktop processor")
                .dateOfManufacture(LocalDate.of(2022, 9, 27)).price(350).build();
        return new InventoryItem(product, 3);
    }

    private InventoryItem ssdItem() {
        Product product = Product.builder().id(3).name("Samsung 990 Pro").description("2TB solid state drive")
                .dateOfManufacture(LocalDate.of(2022, 11, 22)).price(170).build();
        return new InventoryItem(product, 10);
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

    @Test
    void getAllReturnsEveryItem() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        List<InventoryItem> items = manager.getAllInventoryItems();

        //Assert
        assertEquals(3, items.size());
    }

    @Test
    void findsItemById() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        InventoryItem found = manager.getInventoryItemByProductId(2);

        //Assert
        assertEquals("Ryzen 9 7900X", found.getProduct().getName());
    }

    @Test
    void unknownIdReturnsNull() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        InventoryItem found = manager.getInventoryItemByProductId(99);

        //Assert
        assertNull(found);
    }

    @Test
    void searchByNameFindsMatch() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        List<InventoryItem> results = manager.searchProductsByName("samsung");

        //Assert
        assertEquals(1, results.size());
        assertEquals(3, results.get(0).getProduct().getId());
    }

    @Test
    void searchByDescriptionFindsMatch() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        List<InventoryItem> results = manager.searchProductsByDescription("graphics");

        //Assert
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getProduct().getId());
    }

    @Test
    void searchIgnoresCase() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        List<InventoryItem> results = manager.searchProductsByName("RYZEN");

        //Assert
        assertEquals(1, results.size());
    }
}