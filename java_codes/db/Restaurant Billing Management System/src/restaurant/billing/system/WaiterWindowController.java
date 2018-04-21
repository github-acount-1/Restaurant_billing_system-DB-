//somehow MainUI is null when initialize is called ??
package restaurant.billing.system;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class WaiterWindowController implements Initializable {

    private BillingSystem mainUI;
    private ArrayList<ObservableList<MenuModel>> tableOrders; //for each table
    private ArrayList<Float> currentTotals; //for each table
//    private int userID;
    private ServerSocket serv;
    
    // The waiter model who has logged in
    private WaiterModel waiter;

    @FXML
    private TableView<MenuModel> ordersTable;
    @FXML
    private TableColumn<MenuModel, String> itemName;
    @FXML
    private TableColumn<MenuModel, String> itemType;
    @FXML
    private TableColumn<MenuModel, Float> price;
    @FXML
    private TableColumn<MenuModel, Integer> quantity;

    @FXML
    private ComboBox<MenuModel> cbItemName;
    @FXML
    private ComboBox<Integer> cbTableNumber;

    @FXML
    private TextField tfQuantity;
    @FXML
    private MenuButton userName;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label currentTotal;
    @FXML
    private Label taxIncluded;
    @FXML
    private Label tableNumberLabel;

    public void setApplicationClass(BillingSystem app) {
        this.mainUI = app;
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        DBConnection dbConn = new DBConnection();
        tableOrders = new ArrayList<>();
        currentTotals = new ArrayList<>(BillingSystem.tableNumbers);

        updateTime();

        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //to use the whole MenuModel instead of just a string
        cbItemName.setCellFactory((ListView<MenuModel> param) -> new ListCell<MenuModel>() {
            @Override
            protected void updateItem(MenuModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getItemName());
                }
            }
        });

        dbConn.close();
    }
        
    public void startWaiterServer() {
        Task<Void> task = new Task<Void>() {
            @Override 
            protected Void call() {

                try {
                    serv = new ServerSocket(11186);
                    System.out.println("waiter listening...");

                    while (true) {
                        Socket sock = serv.accept();
                        DataInputStream input = new DataInputStream(sock.getInputStream());
                        int tableNumber = input.readInt();
                        System.out.println("Received table " + tableNumber);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(tableNumber);
                            }
                        });
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                stopServer();
                
                return null;
            }
        };
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        }).start();
        
    }
    
    public void updateUI(int tableNumber) {
        tableOrders.get(tableNumber - 1).clear();
    }
    
    public void stopServer() {
        try {
            if (serv != null && !serv.isClosed()) {
                serv.close();
                System.out.println("Server is Closed. (port=11186)");
            }
        } catch (Exception e) {
        }
    }
    
    public void populateBillingWindow() {
        DBConnection dbConn = new DBConnection();
        try {
            ResultSet itemsList = dbConn.executeQuery("SELECT * FROM menu");
            while (itemsList.next()) {
                cbItemName.getItems().add(new MenuModel(
                        itemsList.getInt("id"), 
                        itemsList.getString("item_name"), 
                        itemsList.getString("item_type"), 
                        itemsList.getFloat("price"), 
                        0));
            }
            
            ResultSet waitersList = dbConn.executeQuery("SELECT * FROM waiters WHERE first_name = '" + this.userName.getText() + "'");
            waitersList.next();
            waiter = new WaiterModel(
                    waitersList.getInt("id"), 
                    waitersList.getString("first_name"), 
                    waitersList.getString("last_name"), 
                    waitersList.getString("time"));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= BillingSystem.tableNumbers; i++) {
            cbTableNumber.getItems().add(i);
            tableOrders.add(i - 1, FXCollections.observableArrayList());
            currentTotals.add(i - 1, 0f);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        cbItemName.setValue(null);
        cbTableNumber.setValue(null);

        tableNumberLabel.setText("");
        tfQuantity.setText("");
        currentTotal.setText("");
        taxIncluded.setText("");

        for (int i = 0; i < tableOrders.size(); i++) {
            tableOrders.get(i).clear();
            currentTotals.set(i, 0f);
        }

        stopServer();
        mainUI.switchToLogIn();
    }
    
    public void sendOrderToCashierServer(int tableNumber, float currentTotal, ObservableList<MenuModel> orders_list) {
        try {
            Socket sock = new Socket("localhost", 11185);
            SerializableOrderedItemsData obj = new SerializableOrderedItemsData(
                    tableNumber,
                    currentTotal,
                    waiter,
                    orders_list
            );
            ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
            output.writeObject(obj);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addOrder(ActionEvent event) {
        
        updateTime();
        
        int quant;
        Integer tableNum = cbTableNumber.getSelectionModel().getSelectedItem();
        MenuModel selectedItem = cbItemName.getSelectionModel().getSelectedItem();

        try {
            quant = Integer.parseInt(tfQuantity.getText());
        } catch (NumberFormatException e) {
            return;
        }

        if (tableNum == null || quant == 0) {
            return; //zero quantity not allowed
        }
        ObservableList<MenuModel> currentData = tableOrders.get(tableNum - 1);
        float currentTotalPrice = currentTotals.get(tableNum - 1), newTotal = 0;
        boolean alreadyExists = false;

        for (MenuModel orderedItem : currentData) {
            if (orderedItem.getId() == selectedItem.getId()) {
                newTotal = currentTotalPrice + selectedItem.getPrice() * (quant - orderedItem.getQuantity());
                currentData.add(new MenuModel(orderedItem.getId(), orderedItem.getItemName(), 
                                              orderedItem.getItemType(), selectedItem.getPrice() * quant, quant));
                currentData.remove(orderedItem);
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            currentData.add(new MenuModel(selectedItem.getId(), selectedItem.getItemName(), selectedItem.getItemType(), 
                                          selectedItem.getPrice() * quant, quant));
            newTotal = currentTotalPrice + selectedItem.getPrice() * quant;
        }

        currentTotals.set(tableNum - 1, newTotal);
        currentTotal.setText("" + roundToTwo(newTotal) + " Birr");
        taxIncluded.setText("" + roundToTwo(newTotal * 1.15f) + " Birr"); //15%
        
        sendOrderToCashierServer(tableNum, currentTotals.get(tableNum-1), currentData);
    }

    @FXML
    private void removeSelectedOrder(ActionEvent event) {
         
        updateTime();
        
        Integer tableNum = cbTableNumber.getSelectionModel().getSelectedItem();
        ObservableList<MenuModel> currentData = tableOrders.get(tableNum - 1);
        MenuModel selectedItem = ordersTable.getSelectionModel().getSelectedItem();
        if (tableNum != null && selectedItem != null) {
            float currentTotalPrice = currentTotals.get(tableNum - 1);
            float newTotal;

            if (currentData.size() == 1) {
                newTotal = 0;
            } else {
                newTotal = currentTotalPrice - selectedItem.getPrice();
            }

            currentTotals.set(tableNum - 1, roundToTwo(newTotal));
            currentTotal.setText("" + roundToTwo(newTotal) + " Birr");
            taxIncluded.setText("" + roundToTwo(newTotal * 1.15f) + " Birr"); //15%

            tableOrders.get(tableNum - 1).remove(selectedItem);
        }
        
        sendOrderToCashierServer(tableNum, currentTotals.get(tableNum-1), currentData);
    }

    private float roundToTwo(float n) {
        int temp = Math.round(n * 100);
        return temp / 100.0f;
    }

    @FXML
    private void onTableNumberChanged(ActionEvent event) {
         
        updateTime();
        
        Integer tableNum = cbTableNumber.getSelectionModel().getSelectedItem();
        if (tableNum == null || tableNum == 0) {
            return;
        }

        tableNumberLabel.setText(tableNum + "");
        ordersTable.setItems(tableOrders.get(tableNum - 1));

        float currentTotalPrice = currentTotals.get(tableNum - 1);
        currentTotal.setText("" + roundToTwo(currentTotalPrice) + " Birr");
        taxIncluded.setText("" + roundToTwo(currentTotalPrice * 1.15f) + " Birr"); //15%
    }
    
    private void updateTime() {
        int hr = LocalTime.now().getHour();
        int min = LocalTime.now().getMinute();
        timeLabel.setText((hr == 0 || hr == 12 ? 12 : (hr % 12 < 10 ? "0"+(hr%12) : hr%12 )) + ":" + (min < 10 ? "0"+min : min) + (hr >= 12 ? " PM" : " AM"));
        dateLabel.setText(LocalDate.now().toString());
    }

}
