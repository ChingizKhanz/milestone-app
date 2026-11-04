package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.ProductDto;

/** Converts between ProductDto and Product. */
public final class ProductMapper {

    private ProductMapper() {
    }

    /** 
     * Builds a Product from a DTO and turns the date text into a LocalDate.
     * @param dto the product as read from JSON
     * @return the domain product
     */
    public static Product toProduct(ProductDto dto) {
        return Product.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .dateOfManufacture(LocalDate.parse(dto.dateOfManufacture()))
                .price(dto.price())
                .category(dto.category())
                .build();
    }

    /**
     * Builds a DTO from a Product and turns the date back into text.
     * @param product the domain product
     * @return the product as written to JSON
     */
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getDateOfManufacture().toString(),
                product.getPrice(),
                product.getCategory());
    }
}