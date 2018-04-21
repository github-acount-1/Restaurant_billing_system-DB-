//Data Model for the menu table
package restaurant.billing.system;

import java.io.Serializable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import restaurant.billing.system.SerializableMenuModel;

public class MenuModel implements Serializable {

    private IntegerProperty id;
    private StringProperty itemName;
    private StringProperty itemType;
    private FloatProperty price;
    private IntegerProperty quantity;
    
    public MenuModel(int id, String itemName, String itemType, float price, int quantity) {
        this.id = new SimpleIntegerProperty(id);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemType = new SimpleStringProperty(itemType);
        this.price = new SimpleFloatProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }
    
    public static SerializableMenuModel[] serializeAll(ObservableList<MenuModel> menuModels) {
        SerializableMenuModel[] newModels = new SerializableMenuModel[menuModels.size()];
        int i = 0;
        for (MenuModel menuModel : menuModels) {
            newModels[i] = menuModel.getSerializableMenuModel();
            i++;
        }
        return newModels;
    }
    
    public static ObservableList<MenuModel> unserializeAll(SerializableMenuModel[] menuModels) {
        ObservableList<MenuModel> newModels = FXCollections.observableArrayList();
        for (SerializableMenuModel menuModel : menuModels) {
            newModels.add(menuModel.getMenuModel());
        }
        return newModels;
    }
    
    public SerializableMenuModel getSerializableMenuModel() {
        return new SerializableMenuModel(id, itemName, itemType, price, quantity);
    }

    public int getId() {
        return id.get();
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

    public void setPrice(float price) {
        this.price.set(price);
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setItemType(String itemType) {
        this.itemType.set(itemType);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return itemName.get();
    }
    
}
