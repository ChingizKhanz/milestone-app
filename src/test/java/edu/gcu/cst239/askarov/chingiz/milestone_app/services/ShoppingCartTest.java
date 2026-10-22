package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

class ShoppingCartTest {

    private Product gpu() {
        return Product.builder().id(1).name("GeForce RTX 4070").description("graphics card")
                .dateOfManufacture(LocalDate.of(2023, 4, 12)).price(550).build();
    }

    private Product cpu() {
        return Product.builder().id(2).name("Ryzen 9 7900X").description("12-core desktop processor")
                .dateOfManufacture(LocalDate.of(2022, 9, 27)).price(350).build();
    }

    private InventoryManager inventory() {
        return new InventoryManager(List.of(new InventoryItem(gpu(), 5), new InventoryItem(cpu(), 3)));
    }

    @Test
    void addNewProductToCart() {
        // Arange
        CartService cart = new ShoppingCart(inventory());

        // Act
        boolean added = cart.addProduct(gpu(), 2);

        // Assert
        assertTrue(added);
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(2, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void addSameProductTwiceCombines() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());

        // Act
        cart.addProduct(gpu(), 2);
        cart.addProduct(gpu(), 1);

        // Assert
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(3, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void updateQuantityChangesAmount() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());
        cart.addProduct(gpu(), 2);

        // Act
        boolean updated = cart.updateQuantity(1, 4);

        // Assert
        assertTrue(updated);
        assertEquals(4, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void rejectsZeroAndNegativeQuantity() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());

        // Act
        boolean zero = cart.addProduct(gpu(), 0);
        boolean negative = cart.addProduct(gpu(), -1);

        // Assert
        assertFalse(zero);
        assertFalse(negative);
        assertTrue(cart.getAllCartItems().isEmpty());
    }

    @Test
    void removeProductFromCart() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());
        cart.addProduct(gpu(), 2);

        // Act
        boolean removed = cart.removeProductFromCart(1);

        // Assert
        assertTrue(removed);
        assertTrue(cart.getAllCartItems().isEmpty());
    }

    @Test
    void cartTotalAddsSubtotals() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());

        // Act
        cart.addProduct(gpu(), 2);
        cart.addProduct(cpu(), 1);

        // Assert
        assertEquals(1450.0, cart.getCartTotal(), 0.001);
    }

    @Test
    void emptyCartHasNoItemsAndZeroTotal() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());

        // Assert
        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(0.0, cart.getCartTotal(), 0.001);
    }

    @Test
    void successfulCheckoutReducesInventory() {
        // Arrange
        InventoryManager inventory = inventory();
        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(gpu(), 2);
        cart.addProduct(cpu(), 1);

        // Act
        boolean done = cart.checkout();

        // Assert
        assertTrue(done);
        assertEquals(3, inventory.getInventoryItemByProductId(1).getQuantityInStock());
        assertEquals(2, inventory.getInventoryItemByProductId(2).getQuantityInStock());
    }

    @Test
    void successfulCheckoutEmptiesCart() {
        // Arrange
        InventoryManager inventory = inventory();
        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(gpu(), 2);

        // Act
        cart.checkout();

        // Assert
        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(0.0, cart.getCartTotal(), 0.001);
        assertEquals(3, inventory.getInventoryItemByProductId(1).getQuantityInStock());
    }

    @Test
    void notEnoughStockBlocksCheckout() {
        // Arrange
        InventoryManager inventory = inventory();
        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(gpu(), 2);
        inventory.updateQuantity(1, 1);

        // Act
        boolean done = cart.checkout();

        // Assert
        assertFalse(done);
        assertEquals(1, cart.getAllCartItems().size());
    }

    @Test
    void failedCheckoutDoesNotPartlyReduceInventory() {
        // Arrange
        InventoryManager inventory = inventory();
        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(gpu(), 2);
        cart.addProduct(cpu(), 3);
        inventory.updateQuantity(2, 1);

        // Act
        boolean done = cart.checkout();

        // Assert
        assertFalse(done);
        assertEquals(5, inventory.getInventoryItemByProductId(1).getQuantityInStock());
        assertEquals(1, inventory.getInventoryItemByProductId(2).getQuantityInStock());
    }

    @Test
    void emptyCartCheckoutReturnsFalse() {
        // Arrange
        CartService cart = new ShoppingCart(inventory());

        // Act
        boolean done = cart.checkout();

        // Assert
        assertFalse(done);
    }
}