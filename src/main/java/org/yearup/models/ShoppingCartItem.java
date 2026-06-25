package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product = null;
    private int quantity = 1;
    private double discountPercent = 0;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @JsonIgnore
    public int getProductId() {
        return this.product.getProductId();
    }

    public BigDecimal getLineTotal()
    {
        BigDecimal basePrice = product.getPrice();
        BigDecimal subTotal = basePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountAmount =subTotal.multiply(BigDecimal.valueOf(discountPercent));

        return subTotal.subtract(discountAmount);
    }
}
