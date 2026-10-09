package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

/**
 * Manages the inventory of the store in memory.
 * Owns the list of inventory items and answers any question the menu is asked
 * about.
 * Never reads from or writes to the console.
 */
public class InventoryManager implements InventoryService {

    private final List<InventoryItem> inventory;

    public InventoryManager() {
        inventory = new ArrayList<>();
        initializeInventory();
    }

    private void initializeInventory() {
        Product gpu = Product.builder()
                .id(1)
                .name("GeForce RTX 4070")
                .description("12GB GDDR7 graphics card")
                .dateOfManufacture(LocalDate.of(2023, 4, 12))
                .price(550)
                .category("gpu")
                .build();

        Product cpu = Product.builder()
                .id(2)
                .name("Ryzen 9 7900X")
                .description("12-core, 24-thread desktop processor")
                .dateOfManufacture(LocalDate.of(2022, 9, 27))
                .price(350)
                .category("cpu")
                .build();

        Product ssd = Product.builder()
                .id(3)
                .name("Samsung 990 Pro")
                .description("2TB PCIe Gen4 NVMe M.2 internal solid state drive")
                .dateOfManufacture(LocalDate.of(2022, 11, 22))
                .price(170)
                .category("ssd")
                .build();

        Product ram = Product.builder()
                .id(4)
                .name("Corsair Vengeance 32GB DDR5")
                .description("High-performance DDR5 desktop memory")
                .dateOfManufacture(LocalDate.of(2022, 11, 15))
                .price(105)
                .category("ram")
                .build();

        Product motherboard = Product.builder()
                .id(5)
                .name("MSI Pro B650M")
                .description("Standard ATX B650 motherboard with AM5 socket")
                .dateOfManufacture(LocalDate.of(2022, 10, 12))
                .price(160)
                .category("motherboard")
                .build();

        inventory.add(new InventoryItem(gpu, 5));
        inventory.add(new InventoryItem(cpu, 3));
        inventory.add(new InventoryItem(ssd, 10));
        inventory.add(new InventoryItem(ram, 15));
        inventory.add(new InventoryItem(motherboard, 4));
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return List.copyOf(inventory);
    }

    @Override
    public InventoryItem getInventoryItemByProductId(int productId) {
        for (InventoryItem item : inventory) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<InventoryItem> searchProductsByName(String searchTerm) {
        List<InventoryItem> results = new ArrayList<>();
        String term = searchTerm.toLowerCase();
        for (InventoryItem item : inventory) {
            if (item.getProduct().getName().toLowerCase().contains(term)) {
                results.add(item);
            }
        }
        return results;
    }

    @Override
    public List<InventoryItem> searchProductsByDescription(String searchTerm) {
        List<InventoryItem> results = new ArrayList<>();
        String term = searchTerm.toLowerCase();
        for (InventoryItem item : inventory) {
            if (item.getProduct().getDescription().toLowerCase().contains(term)) {
                results.add(item);
            }
        }
        return results;
    }

    @Override
    public boolean addInventoryItem(InventoryItem item) {
        if (item == null) {
            return false;
        }
        int id = item.getProduct().getId();
        if (getInventoryItemByProductId(id) != null) {
            return false;
        }
        inventory.add(item);
        return true;
    }

    @Override
    public boolean updateProduct(Product updatedProduct) {
        for (int i = 0; i < inventory.size(); i++) {
            InventoryItem old = inventory.get(i);
            if (old.getProduct().getId() == updatedProduct.getId()) {
                int quantity = old.getQuantityInStock();
                inventory.set(i, new InventoryItem(updatedProduct, quantity));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateQuantity(int productId, int quantityInStock) {
        if (quantityInStock < 0) {
            return false;
        }
        for (int i = 0; i < inventory.size(); i++) {
            InventoryItem old = inventory.get(i);
            if (old.getProduct().getId() == productId) {
                Product product = old.getProduct();
                inventory.set(i, new InventoryItem(product, quantityInStock));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeProductById(int productId) {
        InventoryItem item = getInventoryItemByProductId(productId);
        if (item == null) {
            return false;
        }
        inventory.remove(item);
        return true;
    }

    @Override
    public void clearInventory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearInventory'");
    }

    /**
     * Returns a sorted copy by product name. The stored list is not reordered.
     * @return the inventory items in name order
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByName() {
        List<InventoryItem> sorted = new ArrayList<>(inventory);
        sorted.sort(Comparator.comparing(item -> item.getProduct().getName()));
        return sorted;
    }

    /**
     * Returns a sorted copy by date of manufacture. The stored list is not reordered.
     * @return the inventory items from oldest to newest
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByDate() {
        List<InventoryItem> sorted = new ArrayList<>(inventory);
        sorted.sort(Comparator.comparing(item -> item.getProduct().getDateOfManufacture()));
        return sorted;
    }

    /**
     * Returns a sorted copy by price. The stored list is not reordered.
     * @return the inventory items from cheapest to most expensive
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByPrice() {
        List<InventoryItem> sorted = new ArrayList<>(inventory);
        sorted.sort(Comparator.comparingDouble(item -> item.getProduct().getPrice()));
        return sorted;
    }
}