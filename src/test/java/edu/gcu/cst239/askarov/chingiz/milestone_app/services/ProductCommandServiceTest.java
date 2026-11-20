package edu.gcu.cst239.askarov.chingiz.milestone_app.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.askarov.chingiz.milestone_app.models.InventoryItem;
import edu.gcu.cst239.askarov.chingiz.milestone_app.models.Product;

class ProductCommandServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private ProductCommandService service;

    private InventoryItem item(int id, String name, double price) {
        Product product = Product.builder().id(id).name(name).description("test part")
                .dateOfManufacture(LocalDate.of(2023, 1, 1)).price(price).category("gpu").build();
        return new InventoryItem(product, 5);
    }

    @BeforeEach
    void setUp() {
        InventoryService inventory = new InventoryManager(List.of(
                item(1, "Samsung 990 Pro", 170),
                item(2, "GeForce RTX 4070", 550),
                item(3, "Ryzen 9 7900X", 350)));
        service = new ProductCommandService(inventory);
    }

    private JsonNode send(String command) throws Exception {
        return mapper.readTree(service.processMessage(command));
    }

    @Test
    void allReturnsEveryItem() throws Exception {
        JsonNode result = send("ALL");

        assertEquals(3, result.size());
    }

    @Test
    void getReturnsRequestedProduct() throws Exception {
        JsonNode result = send("GET 2");

        assertEquals(1, result.size());
        assertEquals("GeForce RTX 4070", result.get(0).get("product").get("name").asText());
    }

    @Test
    void findIgnoresCase() throws Exception {
        JsonNode result = send("FIND ryzen");

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).get("product").get("id").asInt());
    }

    @Test
    void deleteRemovesItem() throws Exception {
        JsonNode result = send("DELETE 1");

        assertEquals(2, result.size());
        assertEquals(0, send("GET 1").size());
    }

    @Test
    void sortNameReturnsNameOrder() throws Exception {
        JsonNode result = send("SORT NAME");

        assertEquals("GeForce RTX 4070", result.get(0).get("product").get("name").asText());
        assertEquals("Ryzen 9 7900X", result.get(1).get("product").get("name").asText());
        assertEquals("Samsung 990 Pro", result.get(2).get("product").get("name").asText());
    }

    @Test
    void sortPriceReturnsPriceOrder() throws Exception {
        JsonNode result = send("SORT PRICE");

        assertEquals(170, result.get(0).get("product").get("price").asDouble());
        assertEquals(350, result.get(1).get("product").get("price").asDouble());
        assertEquals(550, result.get(2).get("product").get("price").asDouble());
    }

    @Test
    void unknownCommandReturnsError() throws Exception {
        JsonNode result = send("BUY 1");

        assertTrue(result.get(0).asText().startsWith("ERROR"));
    }

    @Test
    void malformedCommandReturnsError() throws Exception {
        JsonNode result = send("GET abc");

        assertTrue(result.get(0).asText().startsWith("ERROR"));
    }
}