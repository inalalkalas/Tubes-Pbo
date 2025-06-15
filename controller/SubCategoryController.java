package controller;

import dao.SubCategoryDAO;
import model.SubCategory;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryController {
    private SubCategoryDAO subCategoryDao = new SubCategoryDAO();

    // Pastikan sub_kategori tersedia
    public int ensureAndGetSubCategoryId(String subCategoryName) {
        if (subCategoryName == null || subCategoryName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama sub-kategori kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        SubCategory existing = subCategoryDao.getSubCategoryByName(subCategoryName);
        if (existing != null) {
            return existing.getSubCategoryID(); // Gunakan yang sudah ada
        } else {
            boolean isAdded = subCategoryDao.addSubCategoryIfNotExists(subCategoryName);
            if (!isAdded) {
                return -1;
            }
            return subCategoryDao.getLastSubCategoryId(); // Ambil ID terbaru
        }
    }

    // Ambil semua sub-kategori sebagai List
    public List<SubCategory> getAllSubCategories() {
        return subCategoryDao.getAllSubCategories();
    }

    // Ambil semua sub-kategori sebagai array
    public SubCategory[] getAllSubCategoriesArray() {
        List<SubCategory> list = subCategoryDao.getAllSubCategories();
        return list.toArray(new SubCategory[0]);
    }

    // Hanya ambil nama sub-kategori untuk JComboBox
    public List<String> getAllSubCategoryNames() {
        List<String> names = new ArrayList<>();
        for (SubCategory s : getAllSubCategories()) {
            names.add(s.getSubCategoryName());
        }
        return names;
    }
}