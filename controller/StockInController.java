package controller;

import dao.StockInDAO;
import model.StockIn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

public class StockInController {
    private StockInDAO stockInDao = new StockInDAO();

    public void loadStockInToTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (StockIn stockIn : stockInDao.getAllStockIn()) {
            model.addRow(new Object[]{
                    stockIn.getStockInID(),
                    stockIn.getProductID(),
                    stockIn.getQuantity(),
                    stockIn.getDate(),
                    stockIn.getSupplierName(),
                    stockIn.getNotes()
            });
        }
    }

    public boolean addStockIn(int productID, int quantity, int userId, String supplierName, String notes) {
        return stockInDao.addStockIn(productID, quantity, userId, supplierName, notes);
    }

    public boolean updateStockIn(StockIn stockIn) {
        return stockInDao.updateStockIn(stockIn);
    }

    public boolean deleteStockIn(int stockInID) {
        return stockInDao.deleteStockIn(stockInID);
    }

    public void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadStockInToTable(model);
    }
}