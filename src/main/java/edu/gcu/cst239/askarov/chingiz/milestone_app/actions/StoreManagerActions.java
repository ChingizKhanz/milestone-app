package edu.gcu.cst239.askarov.chingiz.milestone_app.actions;

import edu.gcu.cst239.askarov.chingiz.milestone_app.services.StoreFront;
import edu.gcu.cst239.askarov.chingiz.milestone_app.util.InputUtilities;

/**
 * Provides the user with the Store Manager menu and asks which option they would like to proceed with.
 */
public class StoreManagerActions {

    private final StoreFront store;

    /**
     * Creates the store manager menu for a storefront.
     * @param store the storefront where the invenotry this menu is managing.
     */
    public StoreManagerActions(StoreFront store) {
        this.store = store;
    }

    /**
     * Shows the store manager menu until the manager chooses to exit.
     * Returns to the role menu if option 0 is selected.
     */
    public void handleManagerActions() {
        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println();
            System.out.println("Store Manager Menu:");
            System.out.println("1. View Products");
            System.out.println("2. Search for a product by name or description");
            System.out.println("3. Add Product to Inventory");
            System.out.println("4. Remove Product from Inventory");
            System.out.println("5. Update Product in Inventory");
            System.out.println("6. Save Inventory to File");
            System.out.println("7. Load Inventory from File");
            System.out.println("0. Exit");
            int choice = input.readInt("Choose an option: ", 0, 7);
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
                    System.out.println("You chose to add a product to inventory.");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 4:
                    System.out.println("You chose to remove a product in inventory.");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 5:
                    System.out.println("You chose to update a product in inventory.");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 6:
                    System.out.println("You chose to save inventory to a file");
                    System.out.println("This is milestone #1.  No actual functionality has been implemented yet.");
                    break;
                case 7:
                    System.out.println("You chose to load inventory from a file");
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
