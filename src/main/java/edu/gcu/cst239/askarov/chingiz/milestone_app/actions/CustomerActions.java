package edu.gcu.cst239.askarov.chingiz.milestone_app.actions;

import edu.gcu.cst239.askarov.chingiz.milestone_app.services.StoreFront;
import edu.gcu.cst239.askarov.chingiz.milestone_app.util.InputUtilities;

/**
 * Provides the user with the Customer menu and asks which option they would like to proceed with.
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
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
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
}
