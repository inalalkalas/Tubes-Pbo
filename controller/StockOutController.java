package controller;

import dao.StockOutDAO;
import model.StockOut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

public class StockOutController {
    private StockOutDAO stockOutDao = new StockOutDAO();

    public void loadStockOutToTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (StockOut stockOut : stockOutDao.getAllStockOut()) {
            model.addRow(new Object[]{
                    stockOut.getStockOutID(),
                    stockOut.getProductID(),
                    stockOut.getQuantity(),
                    stockOut.getDate(),
                    stockOut.getDestination(),
                    stockOut.getNotes()
            });
        }
    }

    public boolean addStockOut(int productID, int quantity, int userId, String destination, String notes) {
        return stockOutDao.addStockOut(productID, quantity, userId, destination, notes);
    }

    public boolean updateStockOut(StockOut stockOut) {
        return stockOutDao.updateStockOut(stockOut);
    }

    public boolean deleteStockOut(int stockOutID) {
        return stockOutDao.deleteStockOut(stockOutID);
    }

    public void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadStockOutToTable(model);
    }
}