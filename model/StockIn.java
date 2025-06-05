package model;

import java.time.LocalDateTime;

public class StockIn {
    private int stockInID;
    private int productID;
    private String productName;
    private int quantity;
    private LocalDateTime date;
    private int userID;
    private String supplierName;
    private String notes;

    public StockIn(int stockInID, int productID, String productName, int quantity,
                  LocalDateTime date, int userID, String supplierName, String notes) {
        this.stockInID = stockInID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.userID = userID;
        this.supplierName = supplierName;
        this.notes = notes;
    }

    // Getter
    public int getStockInID() { return stockInID; }
    public int getProductID() { return productID; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getDate() { return date; }
    public int getUserID() { return userID; }
    public String getSupplierName() { return supplierName; }
    public String getNotes() { return notes; }
}