// only one cashier at a time runs - requirement
// Think of SQL Injection
// Validation for out of range data, item quantity
// amharic menu
// after receipt printed, clear that of waiters window
// description of current window with big label
// prepare cmd line params for dbserver ip, cashier server ip
// show notification if cashier server isn't running
// test with network cable

package restaurant.billing.system;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BillingSystem extends Application {
    
    public static int tableNumbers;
    
    private Stage stage;
    private DBConnection dbConn;
    private Scene loginScene, mainScene, adminScene, detailsScene, waiterScene;
    
    private LoginController cLogin;
    private BillingWindowController cMainWindow;
    private AdminController cAdmin;
    private DetailsController cDetails;
    private WaiterWindowController cWaiterWindow;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        dbConn = new DBConnection();
        if (dbConn.getConnection() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error.");
            alert.setContentText("Unable to Connect to database");
            alert.showAndWait();
            System.exit(0);
        }
        
        ResultSet result = dbConn.executeQuery("SELECT total FROM number_of_tables");
        try {
            if (result.next()) tableNumbers = result.getInt("total");
        } catch(SQLException e) { e.printStackTrace(); }
        
        stage = primaryStage;
        loadFXML();
        
        primaryStage.setTitle("Restaurant Billing and Management System");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public void switchToBillingWindow(String userName, int userID) {
        cMainWindow.setUserID(userID);
        cMainWindow.setUserName(userName);
        cMainWindow.populateBillingWindow();
        cMainWindow.startCashierServer();
        stage.setScene(mainScene);
    }
    
    public void switchToWaiterWindow(String userName, int userID) {
        cWaiterWindow.setUserName(userName);
        cWaiterWindow.populateBillingWindow();
        cWaiterWindow.startWaiterServer();
        stage.setScene(waiterScene);
    }
    
    public void switchToLogIn() {
        stage.setScene(loginScene);
    }
    
    public void switchToAdmin(String adminName) {
        cAdmin.setUserName(adminName);
        cAdmin.readMenuFromDatabase();
        stage.setScene(adminScene);
    }
    
    public void showDetails(int orderID, String date, String time) {
        cDetails.readDetailsFromDatabase(orderID);
        Stage detailsStage = new Stage();
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Receipt printed on " + date + " at " + time);
        detailsStage.setResizable(false);
        detailsStage.initModality(Modality.WINDOW_MODAL);
        detailsStage.initOwner(stage);
        detailsStage.show();
    }
    
    public DBConnection getDBConnection() {
        return dbConn;
    }
    
    public Stage getMainStage() {
        return stage;
    }
    
    private void loadFXML() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        FXMLLoader fxmlLoader3 = new FXMLLoader();
        FXMLLoader fxmlLoader4 = new FXMLLoader();
        FXMLLoader fxmlLoader5 = new FXMLLoader();
        
        URL urlLogin = getClass().getResource("fxml/login.fxml");
        URL urlMainWindow = getClass().getResource("fxml/billing_window.fxml");
        URL urlEditMenu = getClass().getResource("fxml/admin.fxml");
        URL urlDetails = getClass().getResource("fxml/details.fxml");
        URL urlWaiterWindow = getClass().getResource("fxml/waiter_window.fxml");
        
        fxmlLoader1.setBuilderFactory(new JavaFXBuilderFactory());
        fxmlLoader2.setBuilderFactory(new JavaFXBuilderFactory());
        fxmlLoader3.setBuilderFactory(new JavaFXBuilderFactory());
        fxmlLoader4.setBuilderFactory(new JavaFXBuilderFactory());
        fxmlLoader5.setBuilderFactory(new JavaFXBuilderFactory());
        
        fxmlLoader1.setLocation(urlLogin);
        loginScene = new Scene(fxmlLoader1.load());
        cLogin = (LoginController) fxmlLoader1.getController();
        cLogin.setApplicationClass(this);
        
        fxmlLoader2.setLocation(urlMainWindow);
        mainScene = new Scene(fxmlLoader2.load());
        cMainWindow = (BillingWindowController) fxmlLoader2.getController();
        cMainWindow.setApplicationClass(this);
        
        fxmlLoader3.setLocation(urlEditMenu);
        adminScene = new Scene(fxmlLoader3.load());
        cAdmin = (AdminController) fxmlLoader3.getController();
        cAdmin.setApplicationClass(this);
        
        fxmlLoader4.setLocation(urlDetails);
        detailsScene = new Scene(fxmlLoader4.load());
        cDetails = (DetailsController) fxmlLoader4.getController();
        cDetails.setApplicationClass(this);
        
        fxmlLoader5.setLocation(urlWaiterWindow);
        waiterScene = new Scene(fxmlLoader5.load());
        cWaiterWindow = (WaiterWindowController) fxmlLoader5.getController();
        cWaiterWindow.setApplicationClass(this);
    }
    
    @Override
    public void stop() {
        cMainWindow.stopServer();
        cWaiterWindow.stopServer();
        dbConn.close();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
