package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    private InventoryItem ramItem() {
        Product product = Product.builder().id(4).name("Corsair Vengeance 32GB DDR5").description("DDR5 desktop memory")
                .dateOfManufacture(LocalDate.of(2022, 11, 15)).price(105).build();
        return new InventoryItem(product, 15);  
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

    @Test
    void addNewItemIncreasesSize() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        boolean added = manager.addInventoryItem(ramItem());

        //Assert
        assertTrue(added);
        assertEquals(4, manager.getAllInventoryItems().size());
    }

    @Test
    void addDuplicateIdIsRejected() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        boolean added = manager.addInventoryItem(cpuItem());

        //Assert
        assertFalse(added);
        assertEquals(3, manager.getAllInventoryItems().size());
    }

    @Test
    void updateKeepsQuantity() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));
        Product cheaperCpu = Product.builder().id(2).name("Ryzen 9 7900X").description("12-core desktop processor")
                .dateOfManufacture(LocalDate.of(2022, 9, 27)).price(300).build();
        //Act
        boolean updated = manager.updateProduct(cheaperCpu);

        //Assert
        assertTrue(updated);
        InventoryItem item = manager.getInventoryItemByProductId(2);
        assertEquals(300, item.getProduct().getPrice(),0.001);
        assertEquals(3, item.getQuantityInStock());
    }

    @Test
    void removeExistingItem() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        boolean removed = manager.removeProductById(3);

        //Assert
        assertTrue(removed);
        assertEquals(2, manager.getAllInventoryItems().size());
        assertNull(manager.getInventoryItemByProductId(3));
    }

    @Test
    void updateUnknownIdReturnsFalse() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        Product cheaperCpu = Product.builder().id(99).name("Ryzen 9 7900X").description("12-core desktop processor")
                .dateOfManufacture(LocalDate.of(2022, 9, 27)).price(300).build();

        //Act
        boolean updated = manager.updateProduct(cheaperCpu);

        //Assert
        assertFalse(updated);
    }

    @Test
    void removeUnknownIdLeavesInventoryAlone() {
        //Arrange
        InventoryManager manager = new InventoryManager(List.of(gpuItem(), cpuItem(), ssdItem()));

        //Act
        boolean removed = manager.removeProductById(99);

        //Assert
        assertFalse(removed);
        assertEquals(3, manager.getAllInventoryItems().size());
    }


}