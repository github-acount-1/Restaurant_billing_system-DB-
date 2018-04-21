
package restaurant.billing.system;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DetailsController implements Initializable {

    private BillingSystem mainUI;
    
    @FXML private TableColumn<OrderDetailsModel, String> itemName;
    @FXML private TableColumn<OrderDetailsModel, String> itemType;
    @FXML private TableColumn<OrderDetailsModel, Integer> quantity;
    @FXML private TableColumn<OrderDetailsModel, Float> price;
    @FXML private TableView<OrderDetailsModel> ordersTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    public void setApplicationClass(BillingSystem app) {
        this.mainUI = app;
    }

    public void readDetailsFromDatabase(int orderID) {
        ObservableList<OrderDetailsModel> orderData = FXCollections.observableArrayList();
        try {
            DBConnection dbConn = mainUI.getDBConnection();
            ResultSet result = dbConn.executeQuery("SELECT id, item_name, item_type, quantity, total_price FROM individual_orders, menu WHERE individual_orders.item_id = menu.id AND order_id = " + orderID);
            while (result.next()) {
                orderData.add(new OrderDetailsModel(result.getInt("id"), 
                                                    result.getString("item_name"), 
                                                    result.getString("item_type"), 
                                                    result.getFloat("total_price"), 
                                                    result.getInt("quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ordersTable.setItems(orderData);
    }
    
}
