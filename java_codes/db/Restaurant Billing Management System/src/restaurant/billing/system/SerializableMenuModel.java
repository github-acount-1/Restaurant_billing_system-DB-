
package restaurant.billing.system;

import java.io.Serializable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

class SerializableMenuModel implements Serializable {

    private int id;
    private String itemName;
    private String itemType;
    private float price;
    private int quantity;

    public SerializableMenuModel(IntegerProperty id, StringProperty itemName, StringProperty itemType, FloatProperty price, IntegerProperty quantity) {
        this.id = id.get();
        this.itemName = itemName.get();
        this.itemType = itemType.get();
        this.price = price.get();
        this.quantity = quantity.get();
    }
    
    public MenuModel getMenuModel() {
        return new MenuModel(id, itemName, itemType, price, quantity);
    }
    
}