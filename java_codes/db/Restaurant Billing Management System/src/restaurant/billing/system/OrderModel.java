//Data Model for a single customer's order
package restaurant.billing.system;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderModel {

    private SimpleIntegerProperty orderID;
    private SimpleIntegerProperty tableNumber;
    private SimpleFloatProperty totalPrice;
    private SimpleStringProperty time;
    private SimpleStringProperty date;
    private SimpleStringProperty staffMember; //Cashier
    private SimpleStringProperty waiterName;

    public OrderModel(int orderID, int tableNumber, String date, String time, float totalPrice, String waiterName, String staffMember) {
        this.orderID = new SimpleIntegerProperty(orderID);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.time = new SimpleStringProperty(time);
        this.date = new SimpleStringProperty(date);
        this.totalPrice = new SimpleFloatProperty(totalPrice);
        this.staffMember = new SimpleStringProperty(staffMember);
        this.waiterName = new SimpleStringProperty(waiterName);
    }
    
    public int getOrderID() {
        return orderID.get();
    }
    
    public void setOrderID(int id) {
        this.orderID.set(id);
    }

    public int getTableNumber() {
        return tableNumber.get();
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber.set(tableNumber);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTotalItems() {
        return date.get();
    }

    public void setTotalItems(String totalItems) {
        this.date.set(totalItems);
    }

    public float getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getStaffMember() {
        return staffMember.get();
    }

    public void setStaffMember(String staffMember) {
        this.staffMember.set(staffMember);
    }

    public String getWaiterName() {
        return waiterName.get();
    }

    public void setWaiterName(String waiterName) {
        this.waiterName.set(waiterName);
    }

}
