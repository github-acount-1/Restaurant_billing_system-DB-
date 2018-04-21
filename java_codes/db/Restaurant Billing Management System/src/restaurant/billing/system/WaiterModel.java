
package restaurant.billing.system;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import restaurant.billing.system.SerializableWaiterModel;
import restaurant.billing.system.SerializableWaiterModel;

public class WaiterModel implements Serializable {

    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty lastTimeWorked;

    public WaiterModel(int id, String firstName, String lastName, String lastTimeWorked) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.lastTimeWorked = new SimpleStringProperty(lastTimeWorked);
    }
    
    public SerializableWaiterModel getSerializableWaiterModel() {
        return new SerializableWaiterModel(id, firstName, lastName, lastTimeWorked);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getLastTimeWorked() {
        return lastTimeWorked.get();
    }

    public void setLastTimeWorked(String lastTimeWorked) {
        this.lastTimeWorked.set(lastTimeWorked);
    }

    @Override
    public String toString() {
        return firstName.get() + " " + lastName.get();
    }
    
}
