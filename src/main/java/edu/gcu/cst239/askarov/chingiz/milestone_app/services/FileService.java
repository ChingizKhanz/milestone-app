package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductCategoryDto;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductDto;

/** Reads and writes store data from JSON files. */
public class FileService {

    private static final int STARTING_STOCK = 10;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads every category from a JSON file.
     * @param path the categories file
     * @return the category DTOs
     * @throws IOException when the file is missing or not valid JSON
     */
    public List<ProductCategoryDto> readCategories(Path path) throws IOException {
        return mapper.readValue(path.toFile(), new TypeReference<List<ProductCategoryDto>>() { });
    }

    /**
     * Reads every product from a JSON file.
     * @param path the products file
     * @return the product DTOs
     * @throws IOException when the file is missing or not valid JSON
     */
    public List<ProductDto> readProducts(Path path) throws IOException {
        return mapper.readValue(path.toFile(), new TypeReference<List<ProductDto>>() { });
    }

    /**
     * Loads categories first, then checks and maps every product into an inventory item.
     * @param categoriesPath the categories file
     * @param productsPath the products file
     * @return the starting inventory
     * @throws IOException when a file is missing or not valid JSON
     */
    public List<InventoryItem> loadInventory(Path categoriesPath, Path productsPath) throws IOException {
        Set<String> categories = readCategories(categoriesPath).stream()
                .map(ProductCategoryDto::category)
                .collect(Collectors.toSet());

        List<InventoryItem> items = new ArrayList<>();
        for (ProductDto dto : readProducts(productsPath)) {
            validate(dto, categories);
            items.add(new InventoryItem(ProductMapper.toProduct(dto), STARTING_STOCK));
        }
        return items;
    }

    /**
     * Stops loading when a product has a bad value or an unknown category.
     * @param dto the product to check
     * @param categories the known category keys
     */
    public void validate(ProductDto dto, Set<String> categories) {
        if (dto.id() < 1) {
            throw new IllegalArgumentException("Product id must be positive: " + dto.id());
        }
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Product " + dto.id() + " has no name.");
        }
        if (dto.price() <= 0) {
            throw new IllegalArgumentException("Product " + dto.id() + " has a bad price.");
        }
        if (!categories.contains(dto.category())) {
            throw new IllegalArgumentException("Product " + dto.id() + " has unknown category: " + dto.category());
        }
        LocalDate.parse(dto.dateOfManufacture());
    }

    /**
     * Maps every product back to a DTO and writes them to a JSON file.
     * @param path the file to write
     * @param items the current inventory
     * @throws IOException when the file cannot be written
     */
    public void saveInventory(Path path, List<InventoryItem> items) throws IOException {
        List<ProductDto> dtos = new ArrayList<>();
        for (InventoryItem item : items) {
            dtos.add(ProductMapper.toDto(item.getProduct()));
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), dtos);
    }
}