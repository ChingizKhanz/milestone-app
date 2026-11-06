package edu.gcu.cst239.askarov.chingiz.milestone_app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.actions.StartingActions;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.FileService;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.StoreFront;

/**
 * The beginning for the store application
 * Creates the objects and workflow needs and begins.
 */
public class MilestoneApplication {
/**
 * Starts the application.
 *
 * @param args command line arguments, they are not used
 * @throws IOException when a JSON file cannot be read
 */
    public static void main(String[] args) throws IOException {
        System.out.println("Milestone application starting...");

        List<InventoryItem> items = new FileService().loadInventory(
                Path.of("src", "main", "resources", "data", "categories.json"),
                Path.of("src", "main", "resources", "data", "products.json"));
        System.out.println("Loaded " + items.size() + " products from JSON.");

        StoreFront store = new StoreFront(items);
        StartingActions actions = new StartingActions(store);
        actions.start();
    }
}