//somehow MainUI is null when initialize is called ??

package restaurant.billing.system;

import java.awt.image.RenderedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class BillingWindowController implements Initializable {

    private BillingSystem mainUI;
    private ArrayList<ObservableList<MenuModel>> tableOrders; //for each table
    private ArrayList<Float> currentTotals; //for each table
    private int userID;
    
    private WaiterModel[] tableWaiters;
    private ServerSocket serv;

    private Scene printScene;
    private ReceiptWindowController cPrint;

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
    private ComboBox<Integer> cbTableNumber;

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
    @FXML
    private Label waiterLabel;

    public void setApplicationClass(BillingSystem app) {
        this.mainUI = app;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL urlReceipt = getClass().getResource("fxml/receipt_window.fxml");
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            fxmlLoader.setLocation(urlReceipt);
            printScene = new Scene(fxmlLoader.load());
            cPrint = (ReceiptWindowController) fxmlLoader.getController();
        } catch (IOException e) {
        }

        DBConnection dbConn = new DBConnection();
        tableOrders = new ArrayList<>();
        currentTotals = new ArrayList<>(BillingSystem.tableNumbers);

        updateTime();

        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
 
        tableWaiters = new WaiterModel[BillingSystem.tableNumbers];
        for (WaiterModel indivWaiter : tableWaiters)
            indivWaiter = null;
 
        dbConn.close();
    }
    
    public void startCashierServer() {
        Task<Void> task = new Task<Void>() {
            @Override 
            protected Void call() {

                try {
                    serv = new ServerSocket(11185);
                    System.out.println("listening...");

                    while (true) {
                        Socket sock = serv.accept();
                        ObjectInputStream input = new ObjectInputStream(sock.getInputStream());
                        SerializableOrderedItemsData ordersTaken = (SerializableOrderedItemsData) input.readObject();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(ordersTaken);
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
    
    public void stopServer() {
        try {
            if (serv != null && !serv.isClosed()) {
                serv.close();
                System.out.println("Server is Closed.");
            }
        } catch (Exception e) {
        }
    }
    
    public void updateUI(SerializableOrderedItemsData ordersTaken) {
        
        updateTime();
        
        int tableNum = ordersTaken.getTableNumber();
        float newTotal = ordersTaken.getCurrentTotal();
        
        tableWaiters[tableNum - 1] = ordersTaken.getWaiter();
        currentTotals.set(tableNum - 1, newTotal);
        tableOrders.set(tableNum - 1, ordersTaken.getTableOrders());
        
        waiterLabel.setText(tableWaiters[tableNum - 1].getFirstName());
        currentTotal.setText("" + roundToTwo(newTotal) + " Birr");
        taxIncluded.setText("" + roundToTwo(newTotal * 1.15f) + " Birr"); //15%
        
        cbTableNumber.setValue(tableNum);
        ordersTable.setItems(tableOrders.get(tableNum - 1));
    }
    
    public void sendPrintNotification(int tableNumber) {
        System.out.println("Sending for table " + tableNumber);
        try {
            Socket sock = new Socket("localhost", 11186);
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            output.writeInt(tableNumber);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populateBillingWindow() {
        for (int i = 1; i <= BillingSystem.tableNumbers; i++) {
            cbTableNumber.getItems().add(i);
            tableOrders.add(i - 1, FXCollections.observableArrayList());
            currentTotals.add(i - 1, 0f);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopServer();
    }

    @FXML
    private void logout(ActionEvent event) {
        cbTableNumber.setValue(null);

        tableNumberLabel.setText("");
        currentTotal.setText("");
        taxIncluded.setText("");

        for (int i = 0; i < tableOrders.size(); i++) {
            tableOrders.get(i).clear();
            currentTotals.set(i, 0f);
        }
        
        stopServer();
        mainUI.switchToLogIn();
    }

    // also print to the default printer - foxit reader
    private void saveReceiptSnapshot() {
        try {
            WritableImage snapshot = printScene.snapshot(null);
            File file = new File("MAKrestaurantbill.png");
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(renderedImage, "png", file);
            Runtime.getRuntime().exec("mspaint /p MAKrestaurantbill.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printReceipt(
            ObservableList<MenuModel> orderedItems, 
            int orderID, float beforeVAT, float tax, 
            float total, String cashier, String waiter, String date) 
    {
        cPrint.addOrderedItems(orderedItems, orderID + "", beforeVAT + " Birr", tax + " Birr", total + " Birr", cashier, waiter, date);
        Scene scene = new Scene(new ProgressIndicator());
        scene.setFill(Color.TRANSPARENT);
        
        /* Loading...
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initOwner(mainUI.getMainStage());
        stage.showAndWait(); 
        */

        saveReceiptSnapshot();
        
        //Display code for the receipt window
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Receipt");
        stage.setScene(printScene);
        stage.showAndWait();

    }
        
    @FXML
    private void printReceipt(ActionEvent event) {
         
        updateTime();
        
        Integer tableNum = cbTableNumber.getSelectionModel().getSelectedItem();
        WaiterModel waiter = tableWaiters[tableNum - 1];

        if (tableNum == null || waiter == null) {
            return;
        }

        ObservableList<MenuModel> orderedItems = tableOrders.get(tableNum - 1);
        Float currentTotalPrice = currentTotals.get(tableNum - 1);

        DBConnection dbConn = mainUI.getDBConnection();
        String sql1 = "INSERT INTO orders_list (table_number,total_price,staff_member_id,waiter_id,date,time) VALUES (" 
                + tableNum + ", " 
                + currentTotalPrice + ", " 
                + userID + ", " 
                + waiter.getId() + ", " 
                + "now(), now())";
        String sql2 = "SELECT max(order_id) as order_id FROM orders_list";

        dbConn.executeUpdate(sql1);
        ResultSet result = dbConn.executeQuery(sql2);
        int orderID = 0;
        try {
            if (result != null && result.next()) {
                orderID = result.getInt("order_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (MenuModel orderedItem : orderedItems) {
            String individualOrder = "INSERT INTO individual_orders (order_id,item_id,quantity,total_price) VALUES (" 
                    + orderID + ", "
                    + orderedItem.getId() + ", " 
                    + orderedItem.getQuantity() + ", " 
                    + orderedItem.getPrice() + ")";
            dbConn.executeUpdate(individualOrder);
        }

        printReceipt(orderedItems, orderID, roundToTwo(currentTotalPrice), 
                roundToTwo(0.15f * currentTotalPrice), roundToTwo(1.15f * currentTotalPrice), 
                userName.getText(), waiter.getFirstName(), LocalDate.now().toString());
        
        sendPrintNotification(tableNum);
        orderedItems.clear();
        
        currentTotals.set(tableNum - 1, 0f);
        currentTotal.setText("0.0");
        taxIncluded.setText("0.0");
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
        
        WaiterModel currentWaiter = tableWaiters[tableNum - 1];
        if (currentWaiter == null) {
            waiterLabel.setText(" - ");
        } else {
            waiterLabel.setText(currentWaiter.getFirstName());
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
