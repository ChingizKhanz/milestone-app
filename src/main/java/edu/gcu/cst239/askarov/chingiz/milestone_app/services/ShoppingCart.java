package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.util.ArrayList;
import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.CartItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

/**
 * Keeps the customer's cart in memory.
 * Owns the list of cart items and checks stock through the shared InventoryService.
 */
public class ShoppingCart implements CartService {

    private final InventoryService inventoryService;
    private final List<CartItem> cartItems;

    /**
     * Creates an empty cart that uses the store's inventory.
     *
     * @param inventoryService the shared inventory used to check and update stock
     */
    public ShoppingCart(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.cartItems = new ArrayList<>();
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return List.copyOf(cartItems);
    }

    @Override
    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity < 1) {
            return false;
        }
        int wanted = quantity;
        CartItem existing = findCartItem(product.getId());
        if (existing != null) {
            wanted = wanted + existing.getQuantity();
        }
        if (wanted > getStock(product.getId())) {
            return false;
        }
        if (existing == null) {
            cartItems.add(new CartItem(product, quantity));
        } else {
            existing.setQuantity(wanted);
        }
        return true;
    }

    @Override
    public boolean updateQuantity(int productId, int quantity) {
        CartItem existing = findCartItem(productId);
        if (existing == null || quantity < 0) {
            return false;
        }
        if (quantity == 0) {
            cartItems.remove(existing);
            return true;
        }
        if (quantity > getStock(productId)) {
            return false;
        }
        existing.setQuantity(quantity);
        return true;
    }

    @Override
    public boolean removeProductFromCart(int productId) {
        CartItem existing = findCartItem(productId);
        if (existing == null) {
            return false;
        }
        cartItems.remove(existing);
        return true;
    }

    @Override
    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total = total + item.getSubtotal();
        }
        return total;
    }

    @Override
    public boolean checkout() {
        if (cartItems.isEmpty()) {
            return false;
        }
        for (CartItem item : cartItems) {
            if (item.getQuantity() > getStock(item.getProduct().getId())) {
                return false;
            }
        }
        for (CartItem item : cartItems) {
            int id = item.getProduct().getId();
            inventoryService.updateQuantity(id, getStock(id) - item.getQuantity());
        }
        clearCart();
        return true;
    }

    @Override
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Finds the cart item for a product ID.
     *
     * @param productId the product identifier
     * @return the cart item, or null when it is not in the cart
     */
    private CartItem findCartItem(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    /**
     * Looks up how many of a product are in stock right now.
     *
     * @param productId the product identifier
     * @return the quantity in stock, or 0 when the product is not in inventory
     */
    private int getStock(int productId) {
        InventoryItem stock = inventoryService.getInventoryItemByProductId(productId);
        if (stock == null) {
            return 0;
        }
        return stock.getQuantityInStock();
    }
}
