package model;
import java.sql.Date;

public class User {
    private int userID;
    private String userName;
    private String password;
    private String role;
    private Date lastLogin;
    private String status;
    private String email;

    // Constructor
    public User(int userID, String userName, String password, String role, Date lastLogin, String status, String email) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.lastLogin = lastLogin;
        this.status = status;
        this.email = email;
    }

    public User(int userID, String userName, String password, String role){
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public User(String userName, String password, String role){
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
