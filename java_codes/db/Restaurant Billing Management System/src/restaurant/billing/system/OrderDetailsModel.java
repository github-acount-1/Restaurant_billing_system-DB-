//Data Model for a single order before printing receipt
package restaurant.billing.system;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderDetailsModel {

    IntegerProperty orderID;
    StringProperty itemName;
    StringProperty itemType;
    FloatProperty price;
    IntegerProperty quantity;
    
    public OrderDetailsModel(int id, String itemName, String itemType, float price, int quantity) {
        this.orderID = new SimpleIntegerProperty(id);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemType = new SimpleStringProperty(itemType);
        this.price = new SimpleFloatProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getId() {
        return orderID.get();
    }

    public int getQuantity() {
        return quantity.get();
    }
    
    public String getItemName() {
        return itemName.get();
    }
    
    public String getItemType() {
        return itemType.get();
    }

    public float getPrice() {
        return price.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setItemType(String itemType) {
        this.itemType.set(itemType);
    }

    public void setPrice(float price) {
        this.price.set(price);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setId(int id) {
        this.orderID.set(id);
    }
    
}
