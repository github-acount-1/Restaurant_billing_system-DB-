
package restaurant.billing.system;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

class SerializableWaiterModel implements Serializable {

    public int id;
    public String firstName;
    public String lastName;
    public String lastTimeWorked;
    
    public SerializableWaiterModel(IntegerProperty id, StringProperty firstName, StringProperty lastName, StringProperty lastTimeWorked) {
        this.id = id.get();
        this.firstName = firstName.get();
        this.lastName = lastName.get();
        this.lastTimeWorked = lastTimeWorked.get();
    }
    
    public WaiterModel getWaiterModel() {
        return new WaiterModel(id, firstName, lastName, lastTimeWorked);
    }
    
}