package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductDto;

/**
 * Turns text commands into inventory operations and JSON responses.
 * It does not open sockets or use the console, so it can be tested on its own.
 */
public class ProductCommandService {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates the command service.
     *
     * @param inventoryService the inventory the commands work on
     */
    public ProductCommandService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Runs one command and returns the result as JSON.
     *
     * @param message the command, for example ALL, GET 5 or SORT PRICE
     * @return a JSON array of inventory items, or a JSON error
     */
    public String processMessage(String message) {
        if (message == null || message.isBlank()) {
            return error("Command is required");
        }

        String[] parts = message.trim().split("\\s+");
        String command = parts[0].toUpperCase();

        try {
            return switch (command) {
                case "ALL" -> processAll(parts);
                case "GET" -> processGet(parts);
                case "FIND" -> processFind(parts);
                case "DELETE" -> processDelete(parts);
                case "SORT" -> processSort(parts);
                default -> error("Unknown command");
            };
        } catch (IllegalArgumentException ex) {
            return error(ex.getMessage());
        }
    }

    private String processAll(String[] parts) {
        requireLength(parts, 1, "Usage: ALL");
        return json(inventoryService.getAllInventoryItems());
    }

    private String processGet(String[] parts) {
        requireLength(parts, 2, "Usage: GET id");
        InventoryItem item = inventoryService.getInventoryItemByProductId(parseInt(parts[1], "Invalid id"));
        return json(item == null ? List.of() : List.of(item));
    }

    private String processFind(String[] parts) {
        requireLength(parts, 2, "Usage: FIND text");
        return json(inventoryService.searchProductsByName(parts[1]));
    }

    private String processDelete(String[] parts) {
        requireLength(parts, 2, "Usage: DELETE id");
        if (!inventoryService.removeProductById(parseInt(parts[1], "Invalid id"))) {
            return error("Product not found");
        }
        return json(inventoryService.getAllInventoryItems());
    }

    private String processSort(String[] parts) {
        requireLength(parts, 2, "Usage: SORT NAME or SORT PRICE");
        return switch (parts[1].toUpperCase()) {
            case "NAME" -> json(inventoryService.getInventoryItemsSortedByName());
            case "PRICE" -> json(inventoryService.getInventoryItemsSortedByPrice());
            default -> error("Usage: SORT NAME or SORT PRICE");
        };
    }

    private int parseInt(String text, String errorMessage) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void requireLength(String[] parts, int length, String message) {
        if (parts.length != length) {
            throw new IllegalArgumentException(message);
        }
    }

    private String json(List<InventoryItem> items) {
        List<ItemJson> rows = new ArrayList<>();
        for (InventoryItem item : items) {
            rows.add(new ItemJson(ProductMapper.toDto(item.getProduct()), item.getQuantityInStock()));
        }
        try {
            return objectMapper.writeValueAsString(rows);
        } catch (JsonProcessingException ex) {
            return error("Unable to create JSON");
        }
    }

    private String error(String message) {
        try {
            return objectMapper.writeValueAsString(List.of("ERROR: " + message));
        } catch (JsonProcessingException ex) {
            return "[\"ERROR\"]";
        }
    }

    /** One inventory item as it is sent to the client. */
    private record ItemJson(ProductDto product, int quantityInStock) {
    }
}
