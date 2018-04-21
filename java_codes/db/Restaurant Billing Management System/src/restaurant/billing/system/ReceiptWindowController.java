
package restaurant.billing.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReceiptWindowController implements Initializable {
    
    @FXML private TableView<MenuModel> receiptTable;
    @FXML private TableColumn<MenuModel, String> itemName;
    @FXML private TableColumn<MenuModel, Integer> quantity;
    @FXML private TableColumn<MenuModel, Float> price;
    
    @FXML private Label orderNumber;
    @FXML private Label beforeVAT;
    @FXML private Label tax;
    @FXML private Label total;
    @FXML private Label cashierName;
    @FXML private Label waiterName;
    @FXML private Label date;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    public void addOrderedItems(ObservableList<MenuModel> orderedItems, 
            String orderNumber, String beforeVAT, String tax, String total, 
            String cashier, String waiter, String date) {
        receiptTable.setItems(orderedItems);
        this.orderNumber.setText(orderNumber);
        this.beforeVAT.setText(beforeVAT);
        this.tax.setText(tax);
        this.total.setText(total);
        this.cashierName.setText(cashier);
        this.waiterName.setText(waiter);
        this.date.setText(date);
    }
    
}
