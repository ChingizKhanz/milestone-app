package edu.gcu.cst239.askarov.chingiz.milestone_app.actions;

import java.time.LocalDate;
import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.InventoryService;
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
        InventoryService inventoryService = store.getInventoryManager();
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
                    printAllItems(inventoryService.getAllInventoryItems(), "No products in the inventory.");
                    break;
                case 2:
                    System.out.println("You chose to search for product by name or description.");
                    int searchBy = input.readInt("Search by 1. Name or 2. Description", 1, 2);
                    String term = input.readString("Enter search term: ");
                    if(searchBy == 1) {
                        printAllItems(inventoryService.searchProductsByName(term), "Nothing was found.");
                    } else {
                        printAllItems(inventoryService.searchProductsByDescription(term), "Nothing was found.");
                    }
                    break;
                case 3:
                    System.out.println("You chose to add a product to inventory.");
                    int newId = input.readInt("Product ID: ", 1, 999999);
                    String newName = input.readString("Name: ");
                    if (newName.isBlank()) {
                        System.out.println("Name is required. Nothing was added.");
                        break;
                    }
                    String newDescription = input.readString("Description: ");
                    LocalDate newDate = input.readLocalDate("Date of manufacture", "yyyy-MM-dd");
                    double newPrice = input.readDouble("Price: ", 0, 100000);
                    String newCategory = input.readString("Category: ");
                    int newQuantity = input.readInt("Starting quantity: ", 0, 100000);

                    Product newProduct = Product.builder()
                            .id(newId)
                            .name(newName)
                            .description(newDescription)
                            .dateOfManufacture(newDate)
                            .price(newPrice)
                            .category(newCategory)
                            .build();
                    InventoryItem newItem = new InventoryItem(newProduct, newQuantity);
                    if (inventoryService.addInventoryItem(newItem)) {
                        System.out.println("Product added.");
                    } else {
                        System.out.println("That product ID already exists. Nothing was added.");
                    }
                    break;
                case 4:
                    System.out.println("You chose to remove a product in inventory.");
                    int removeId = input.readInt("Enter the ID to remove: ");
                    if (inventoryService.removeProductById(removeId)){
                        System.out.println("Product removed.");
                    } else {
                        System.out.println("No product with that ID was found.");
                    }
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
    /**
     * Each item in list is printed, unless the list is empty then a message is printed.
     * @param items all the items to be printed
     * @param emptymessage message when list is empty
     */
    private void printAllItems(List<InventoryItem> items, String emptymessage) {
        if (items.isEmpty()) {
            System.out.println(emptymessage);
            return;
        }
        for (InventoryItem item : items) {
            System.out.println(item);
        }
    }


}
