package model;

import java.time.LocalDateTime;

public class StockOut {
    private int stockOutID;
    private int productID;
    private String productName;
    private int quantity;
    private LocalDateTime date;
    private int userID;
    private String destination;
    private String notes;

    public StockOut(int stockOutID, int productID, String productName, int quantity,
                    LocalDateTime date, int userID, String destination, String notes) {
        this.stockOutID = stockOutID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.userID = userID;
        this.destination = destination;
        this.notes = notes;
    }

    // Getter
    public int getStockOutID() { return stockOutID; }
    public int getProductID() { return productID; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getDate() { return date; }
    public String getDestination() { return destination; }
    public String getNotes() { return notes; }
}