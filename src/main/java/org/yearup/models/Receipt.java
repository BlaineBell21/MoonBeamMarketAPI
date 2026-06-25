package org.yearup.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt {

    private int orderId;
    private LocalDateTime purchaseDate;
    private String customerName;
    private List<OrderItem> items;
    private BigDecimal total;

    public Receipt(int orderId, LocalDateTime purchaseDate, String customerName, List<OrderItem> items, BigDecimal total) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.customerName = customerName;
        this.items = items;
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
