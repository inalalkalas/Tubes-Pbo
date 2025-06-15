package controller;

import dao.CategoryDAO;
import model.Category;
import model.SubCategory;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class CategoryController {
    private CategoryDAO categoryDao = new CategoryDAO();

    // Pastikan kategori tersedia
    public int ensureAndGetCategoryId(String categoryName, String subCategoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama kategori kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        if (subCategoryName == null || subCategoryName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama sub-kategori kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        // Cek apakah kategori sudah ada
        Category existing = categoryDao.getCategoryByName(categoryName);
        if (existing != null) {
            return existing.getCategoryID();
        }

        // Pastikan sub-kategori tersedia dulu
        int subCategoryId = new SubCategoryController().ensureAndGetSubCategoryId(subCategoryName);
        if (subCategoryId == -1) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan atau membuat sub-kategori.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        // Sekarang tambahkan kategori baru
        boolean isAdded = categoryDao.addCategory(categoryName, subCategoryId);
        if (!isAdded) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan kategori baru.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        return categoryDao.getLastCategoryId(); // Ambil ID terbaru
    }

    // Di dalam CategoryController.java
    public String[] getAllCategoryNames() {
        List<Category> categories = categoryDao.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getCategoryName());
        }
        return categoryNames.toArray(new String[0]);
    }

    // Di controller/CategoryController.java
    public Category[] getAllCategories() {
    List<Category> list = categoryDao.getAllCategories();
    return list.toArray(new Category[0]);
}


}