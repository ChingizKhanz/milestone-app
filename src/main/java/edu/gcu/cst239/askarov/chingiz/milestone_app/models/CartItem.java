package edu.gcu.cst239.askarov.chingiz.milestone_app.models;

/**
 * One product in the cart with the quantity the customer wants to buy.
 * This is separate from InventoryItem, which holds the quantity in stock.
 */
public class CartItem {

    private final Product product;
    private int quantity;

    /**
     * Creates a cart item.
     *
     * @param product the product the customer wants
     * @param quantity how many the customer wants to buy
     */
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Returns the product in this cart item.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns how many the customer wants to buy.
     *
     * @return the cart quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Changes how many the customer wants to buy.
     *
     * @param quantity the new cart quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the price of this line, price times quantity.
     *
     * @return the line subtotal
     */
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "ID " + product.getId()
                + ", " + product.getName()
                + ", quantity=" + quantity
                + ", subtotal=$" + String.format("%.2f", getSubtotal());
    }
}
