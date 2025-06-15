package model;

public class SubCategory {
    private int subCategoryID;
    private String subCategoryName;

    public SubCategory(int subCategoryID, String subCategoryName) {
        this.subCategoryID = subCategoryID;
        this.subCategoryName = subCategoryName;
    }

    public int getSubCategoryID() {
        return subCategoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    @Override
    public String toString() {
        return subCategoryName; // Untuk tampil di JComboBox
    }
}