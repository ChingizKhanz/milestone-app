package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;

/**
 * Represents the storefront application and provides access to its
 * major services.
 *
 * StoreFront does not directly store or manipulate inventory.
 * Inventory operations are delegated to InventoryManager.
 */
public class StoreFront {

    private final InventoryService inventoryManager;
    private final CartService cartService;

    /**
     * Creates the storefront and its required services.
     * @param items the starting inventory loaded from JSON
     */
    public StoreFront(List<InventoryItem> items) {
        inventoryManager = new InventoryManager(items);
        cartService = new ShoppingCart(inventoryManager);
    }

    /**
     * Returns the service responsible for managing store inventory.
     *
     * @return the inventory manager
     */
    public InventoryService getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Returns the shared shopping cart.
     * @return the cart service
     */
    public CartService getCartService(){
        return cartService;
    }
}
