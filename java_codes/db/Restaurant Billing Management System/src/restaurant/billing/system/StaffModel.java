//Data Model for a staff member
package restaurant.billing.system;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StaffModel {
    
    private IntegerProperty id;
    private StringProperty userName;
    private StringProperty password;
    private StringProperty accountType;
    private StringProperty lastLogin;
    
    public StaffModel(int id, String userName, String password, int accountType, String lastLogin) {
        this.id = new SimpleIntegerProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.accountType = new SimpleStringProperty(accountType == 0 ? "Cahsier" : (accountType == 1 ? "Manager" : "Waiter"));
        this.lastLogin = new SimpleStringProperty(lastLogin);
    }

    public String getUserName() {
        return this.userName.get();
    }

    public void setID(int id) {
        this.id.set(id);
    }

    public int getID() {
        return this.id.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getPassword() {
        return this.password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getIsManager() {
        return this.accountType.get();
    }

    public void setIsManager(String isManager) {
        this.accountType.set(isManager);
    }

    public String getLastLogin() {
        return lastLogin.get();
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin.set(lastLogin);
    }

}
