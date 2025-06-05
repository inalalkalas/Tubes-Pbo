package model;

public class Product {
    private int productID;
    private String productName;
    private int categoryID;
    private float productPrice;
    private int productStock;
    private String categoryName;
    private String subCategoryName;
    private int productMax = 99; // Default


    // Constructor tanpa nama kategori/sub-kategori (misal dari database)
    public Product(int productID, String productName, int categoryID, float productPrice, int productStock) {
        this(productID, productName, categoryID, productPrice, productStock, "", "");
    }

    // Constructor lengkap dengan kategori & sub-kategori
    public Product(int productID, String productName, int categoryID, float productPrice, int productStock,
                  String categoryName, String subCategoryName) {
        this.productID = productID;
        this.productName = productName != null ? productName : "";
        this.categoryID = categoryID;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.categoryName = categoryName != null ? categoryName : "";
        this.subCategoryName = subCategoryName != null ? subCategoryName : "";
    }

    // Getters
    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    // Setters
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName != null ? productName : "";
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName != null ? categoryName : "";
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName != null ? subCategoryName : "";
    }

    public int getProductMax() {
        return productMax;
    }

    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }
}