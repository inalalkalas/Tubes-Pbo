package model;

public class Category {
    private int categoryID;
    private String categoryName;

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName != null ? categoryName : "";
    }

    // Getter
    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    // Setter
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName != null ? categoryName : "";
    }

    @Override
    public String toString() {
        return categoryName; // Agar JComboBox menampilkan nama
    }
}