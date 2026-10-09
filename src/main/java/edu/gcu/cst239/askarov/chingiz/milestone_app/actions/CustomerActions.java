package edu.gcu.cst239.askarov.chingiz.milestone_app.actions;

import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.CartItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.CartService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.InventoryService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.StoreFront;
import edu.gcu.cst239.askarov.chingiz.milestone_app.util.InputUtilities;

/**
 * Provides the user with the Customer menu and asks which option they would
 * like to proceed with.
 */
public class CustomerActions {

    private final StoreFront store;

    /**
     * Creates the customer menu for a storefront.
     * 
     * @param store the storefront this menu will read products from
     */
    public CustomerActions(StoreFront store) {
        this.store = store;
    }

    /**
     * Shows the customer menu until the customer chooses to exit.
     * Returns control to the role menu when they pick 0.
     */
    public void handleCustomerActions() {
        InputUtilities input = new InputUtilities();
        InventoryService inventoryService = store.getInventoryManager();
        CartService cartService = store.getCartService();
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println();
            System.out.println("Customer Menu:");
            System.out.println("1. View Products");
            System.out.println("2. Search for product by name or description");
            System.out.println("3. Add Product to Cart");
            System.out.println("4. Remove Product from Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("0. Exit");
            int choice = input.readInt("Choose an option: ", 0, 6);
            switch (choice) {
                case 1:
                    System.out.println("You chose to view products.");
                    int sortBy = input.readInt("Sort by 1. Name, 2. Manufacture date or 3. Price", 1, 3);
                    List<InventoryItem> sorted;
                    if (sortBy == 1) {
                        sorted = inventoryService.getInventoryItemsSortedByName();
                    } else if (sortBy == 2) {
                        sorted = inventoryService.getInventoryItemsSortedByDate();
                    } else {
                        sorted = inventoryService.getInventoryItemsSortedByPrice();
                    }
                    printAvailableItems(sorted, "No products are available right now.");
                    break;
                case 2:
                    System.out.println("You chose to search for product by name or description.");
                    int searchBy = input.readInt("Search by 1. Name or 2. Description", 1, 2);
                    String term = input.readString("Enter search term: ");
                    List<InventoryItem> matches;
                    if (searchBy == 1) {
                        matches = inventoryService.searchProductsByName(term);
                    } else {
                        matches = inventoryService.searchProductsByDescription(term);
                    }
                    if (matches.isEmpty()) {
                        System.out.println("Nothing was found.");
                    }
                    for (InventoryItem match : matches) {
                        System.out.println(match);
                    }
                    break;
                case 3:
                    System.out.println("You chose to add products to your cart");
                    printAvailableItems(inventoryService.getAllInventoryItems(),
                            "No products are available right now.");
                    int cartId = input.readInt("Product ID to add: ");
                    InventoryItem wanted = inventoryService.getInventoryItemByProductId(cartId);
                    if (wanted == null) {
                        System.out.println("No product with that ID was found.");
                        break;
                    }
                    int cartQuantity = input.readInt("Quantity: ");
                    if (cartService.addProduct(wanted.getProduct(), cartQuantity)) {
                        System.out.println("Added to cart.");
                    } else {
                        System.out.println("Quantity must be 1 or more and not above stock. Nothing was added.");
                    }
                    break;
                case 4:
                    System.out.println("You chose to remove products from your cart");
                    printCart(cartService);
                    int dropId = input.readInt("Product ID to remove from cart: ");
                    if (cartService.removeProductFromCart(dropId)) {
                        System.out.println("Removed from cart.");
                    } else {
                        System.out.println("That product is not in your cart.");
                    }
                    break;
                case 5:
                    System.out.println("You chose to view your cart");
                    printCart(cartService);
                    if (cartService.getAllCartItems().isEmpty()) {
                        break;
                    }
                    int editId = input.readInt("Product ID to change, or 0 to keep the cart: ");
                    if (editId == 0) {
                        break;
                    }
                    int editQuantity = input.readInt("New quantity (0 removes the item): ");
                    if (cartService.updateQuantity(editId, editQuantity)) {
                        System.out.println("Cart updated.");
                        printCart(cartService);
                    } else {
                        System.out.println("That change is not possible. Nothing was changed.");
                    }
                    break;
                case 6:
                    System.out.println("You chose to checkout.");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 0:
                    System.out.println("You chose to exit.");
                    exitRequested = true;
                    break;
                default:
                    // This case should never occur because readInt enforces range
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    /**
     * Prints the items that are in stock, or a message when there are none.
     * 
     * @param items        the inventory items to check
     * @param emptyMessage the text to print when nothing is in stock
     */
    private void printAvailableItems(List<InventoryItem> items, String emptyMessage) {
        boolean printedAny = false;
        for (InventoryItem item : items) {
            if (item.getQuantityInStock() > 0) {
                System.out.println(item);
                printedAny = true;
            }
        }
        if (!printedAny) {
            System.out.println(emptyMessage);
        }
    }

    private void printCart(CartService cartService) {
        List<CartItem> items = cartService.getAllCartItems();
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        for (CartItem item : items) {
            System.out.println(item);
        }
        System.out.println("Total: $" + String.format("%.2f", cartService.getCartTotal()));
    }
}