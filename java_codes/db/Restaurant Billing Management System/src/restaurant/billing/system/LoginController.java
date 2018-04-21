
package restaurant.billing.system;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController implements Initializable {

    private BillingSystem MainUI;
    
    @FXML private TextField staffPassword;
    @FXML private TextField staffUserName;
    @FXML private Label errorLabel;
    
    public void setApplicationClass(BillingSystem app) {
        this.MainUI = app;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    private void errorAnimation() {
        FadeTransition ft = new FadeTransition(Duration.millis(300), errorLabel);
        ft.setFromValue(1.0);
        ft.setToValue(0.5);
        ft.setAutoReverse(true);
        ft.setCycleCount(4);
        ft.play();
    }
    
    /**** escape user input - protect against sql injection ****/
    private String escape(String str) {
        return str;
    }
    
    @FXML
    private void staffLoginClicked(ActionEvent event) {
        DBConnection dbConn = MainUI.getDBConnection();
        if (dbConn != null) {
            String sql = "SELECT id, isManager FROM staff_members WHERE user_name='" 
                    + escape(staffUserName.getText()) + "' AND password='" 
                    + escape(staffPassword.getText()) + "'";
            ResultSet result = dbConn.executeQuery(sql);
            try {
                //is null if sql statement has syntax errors
                if (result != null && result.next()) {
                    int isManager = result.getInt("isManager");
                    int userID = result.getInt("id");
                    
                    switch(isManager) {
                        case 2: MainUI.switchToWaiterWindow(staffUserName.getText(), userID); break;
                        case 1: MainUI.switchToAdmin(staffUserName.getText()); break;
                        case 0: MainUI.switchToBillingWindow(staffUserName.getText(), userID); break;
                    }
                    
                    String lastLogin = "UPDATE staff_members SET last_login = now() WHERE id = " + userID;
                    dbConn.executeUpdate(lastLogin);
                    
                    staffUserName.setText("");
                    staffPassword.setText("");
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Wrong User Name or Password.");
                    errorAnimation();
                }
            } catch (SQLException e) {
                System.out.println("Staff Login : SQL ERROR");
                e.printStackTrace();
            }
        }
    }
    
}
