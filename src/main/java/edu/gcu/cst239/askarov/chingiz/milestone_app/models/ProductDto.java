package edu.gcu.cst239.askarov.chingiz.milestone_app.models;

/** Flat JSON shape of one product. */
public record ProductDto(
        int id,
        String name,
        String description,
        String dateOfManufacture,
        double price,
        String category) {
}