
package restaurant.billing.system;

import java.io.Serializable;
import javafx.collections.ObservableList;

public class SerializableOrderedItemsData implements Serializable {

    private SerializableMenuModel[] ordersTaken;
    private SerializableWaiterModel waiter;
    private float currentTotal;
    private int tableNumber;
 
    public SerializableOrderedItemsData(int tableNumber, float currentTotal, WaiterModel waiter, ObservableList<MenuModel> ordersTaken) {
        this.ordersTaken = MenuModel.serializeAll(ordersTaken);
        this.waiter = waiter.getSerializableWaiterModel();
        this.currentTotal = currentTotal;
        this.tableNumber = tableNumber;
    }

    public ObservableList<MenuModel> getTableOrders() {
        return MenuModel.unserializeAll(ordersTaken);
    }

    public WaiterModel getWaiter() {
        return waiter.getWaiterModel();
    }

    public float getCurrentTotal() {
        return currentTotal;
    }

    public int getTableNumber() {
        return tableNumber;
    }
    
    @Override
    public String toString() {
        return tableNumber+"";
    }
    
}
