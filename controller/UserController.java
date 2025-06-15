package controller;

import dao.UserDAO;
import model.User;
import view.UserFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserController {
    private UserDAO userDao = new UserDAO();

    // Load semua user ke tabel
    public void loadUsersToTable(DefaultTableModel model) {
        model.setRowCount(0); // Kosongkan tabel
        for (User user : userDao.getAllUsers()) {
            model.addRow(new Object[]{
                    user.getUserID(),
                    user.getUserName(),
                    user.getRole(),
                    user.getEmail()
            });
        }
    }

    // Tambah user baru
    public void addStaff(JFrame parent, DefaultTableModel model) {
        UserFormDialog dialog = new UserFormDialog(parent, "Tambah Staff Baru", true, null);
        if (dialog.isConfirmed()) {
            String name = dialog.getUserName();
            String password = dialog.getPassword();
            String role = dialog.getRole();
            String email = dialog.getEmail();

            // Validasi input
            if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Semua field harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!userDao.isUserNameUnique(name, 0)) {
                JOptionPane.showMessageDialog(parent, "Username sudah ada.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!userDao.isEmailUnique(email, 0)) {
                JOptionPane.showMessageDialog(parent, "Email sudah digunakan.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buat user baru
            User newUser = new User(name, password, role);
            newUser.setEmail(email);

            if (userDao.addUser(newUser)) {
                JOptionPane.showMessageDialog(parent, "Staff berhasil ditambahkan.");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(parent, "Gagal menambahkan staff.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Perbarui tabel
    public void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (User user : userDao.getAllUsers()) {
            model.addRow(new Object[]{
                    user.getUserID(),
                    user.getUserName(),
                    user.getRole(),
                    user.getEmail()
            });
        }
    }

    // Edit user
    public void editStaff(JFrame parent, JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Pilih staff untuk diedit.");
            return;
        }

        int userId = (int) table.getValueAt(selectedRow, 0);
        User existingUser = userDao.getUserById(userId);
        if (existingUser == null) {
            JOptionPane.showMessageDialog(parent, "User tidak ditemukan.");
            return;
        }

        UserFormDialog dialog = new UserFormDialog(parent, "Edit Staff", true, existingUser);
        if (dialog.isConfirmed()) {
            existingUser.setUserName(dialog.getUserName());
            existingUser.setPassword(dialog.getPassword());
            existingUser.setRole(dialog.getRole());
            existingUser.setEmail(dialog.getEmail());

            if (userDao.updateUser(existingUser)) {
                JOptionPane.showMessageDialog(parent, "Staff berhasil diperbarui.");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(parent, "Gagal memperbarui staff.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hapus user
    public void deleteStaff(JFrame parent, JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Pilih staff untuk dihapus.");
            return;
        }

        int userId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(parent, "Yakin ingin menghapus staff ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDao.deleteUser(userId)) {
                JOptionPane.showMessageDialog(parent, "Staff berhasil dihapus.");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(parent, "Gagal menghapus staff.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
}

    
