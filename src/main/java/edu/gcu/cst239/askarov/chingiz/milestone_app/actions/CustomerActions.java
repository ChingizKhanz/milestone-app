package edu.gcu.cst239.askarov.chingiz.milestone_app.actions;

import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.services.InventoryService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
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
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 3:
                    System.out.println("You chose to add products to your cart");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 4:
                    System.out.println("You chose to remove products from your cart");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 5:
                    System.out.println("You chose to view your cart");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
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
    * @param items the inventory items to check
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
}