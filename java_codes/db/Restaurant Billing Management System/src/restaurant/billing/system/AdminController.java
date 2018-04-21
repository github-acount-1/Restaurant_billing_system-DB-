package restaurant.billing.system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class AdminController implements Initializable {

    private BillingSystem mainUI;
    private String fullPath;
    private boolean generateReport = false;
    private boolean ordersSelectedFirstTime = true;
    private boolean manageSelectedFirstTime = true;

    /**
     * ***** Edit Menu ******
     */
    @FXML
    private TableView<MenuModel> menuTable;
    @FXML
    private TableColumn<MenuModel, String> itemName;
    @FXML
    private TableColumn<MenuModel, String> itemType;
    @FXML
    private TableColumn<MenuModel, Float> price;

    @FXML
    private TextField tfItemName;
    @FXML
    private ComboBox<String> cItemType;
    @FXML
    private TextField tfPrice;

    /**
     * ***** Manage Staff *******
     */
    @FXML
    private TableView<StaffModel> staffTable;
    @FXML
    private TableColumn<StaffModel, String> userName;
    @FXML
    private TableColumn<StaffModel, String> password;
    @FXML
    private TableColumn<StaffModel, Boolean> isManager;
    @FXML
    private TableColumn<StaffModel, String> lastLogin;

    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private ComboBox<String> cbStaffType;

    /**
     * ***** Orders List *****
     */
    @FXML
    private TableView<OrderModel> ordersTable;
    @FXML
    private TableColumn<OrderModel, Integer> tableNumber;
    @FXML
    private TableColumn<OrderModel, String> time;
    @FXML
    private TableColumn<OrderModel, String> date;
    @FXML
    private TableColumn<OrderModel, Float> totalPrice;
    @FXML
    private TableColumn<OrderModel, String> waiter;
    @FXML
    private TableColumn<OrderModel, String> staffMember;

    @FXML
    private ComboBox<Integer> cTableNumber;
    @FXML
    private ComboBox<String> cItems;
    @FXML
    private ComboBox<String> cbWaiterName;
    @FXML
    private ComboBox<String> cbCashierName;

    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private MenuButton adminName;

    @FXML
    private TextField tfTotalTables;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * **** Edit Menu *****
         */
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        cItemType.getItems().add("Food");
        cItemType.getItems().add("Drink");

        tfTotalTables.setPromptText(BillingSystem.tableNumbers + "");

        /**
         * ***** Manage Staff ******
         */
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        isManager.setCellValueFactory(new PropertyValueFactory<>("isManager"));
        lastLogin.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

        cbStaffType.getItems().add("Cashier");
        cbStaffType.getItems().add("Waiter");
        cbStaffType.getItems().add("Manager");

        /**
         * ***** Orders List *******
         */
        tableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        date.setCellValueFactory(new PropertyValueFactory<>("totalItems"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        waiter.setCellValueFactory(new PropertyValueFactory<>("waiterName"));
        staffMember.setCellValueFactory(new PropertyValueFactory<>("staffMember"));

        ordersTable.setRowFactory((TableView<OrderModel> tv) -> {
            TableRow<OrderModel> tr = new TableRow<>();
            tr.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    showDetails();
                }
            });
            return tr;
        });

    }

    public void setApplicationClass(BillingSystem app) {
        this.mainUI = app;
    }

    public void setUserName(String adminName) {
        this.adminName.setText(adminName);
    }

    /**
     * ************************ Edit Menu *************************
     */
    public void readMenuFromDatabase() {
        ObservableList<MenuModel> menuData = null;
        try {
            DBConnection dbConn = mainUI.getDBConnection();
            ResultSet result = dbConn.executeQuery("SELECT * FROM menu");
            menuData = FXCollections.observableArrayList();
            while (result.next()) {
                menuData.add(new MenuModel(result.getInt("id"),
                                           result.getString("item_name"),
                                           result.getString("item_type"),
                                           result.getFloat("price"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menuTable.setItems(menuData);
    }

    private void clearEditMenuInput() {
        tfItemName.clear();
        tfPrice.clear();
    }

    @FXML
    private void addToMenuClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        String name = tfItemName.getText();
        String type = cItemType.getSelectionModel().getSelectedItem();
        float priceValue;

        if (name.isEmpty() || type == null) {
            return;
        }
        try {
            priceValue = Float.parseFloat(tfPrice.getText());
        } catch (NumberFormatException e) {
            System.out.println("No float in the string.");
            return;
        }

        if (dbConn.alreadyExists("SELECT id FROM menu WHERE item_name = '" + name + "'")) {
            return;
        }

        String sql = "INSERT INTO menu VALUES (0, '" + name + "', '" + type + "', " + priceValue + ")";
        dbConn.executeUpdate(sql);
        readMenuFromDatabase();
        clearEditMenuInput();
    }

    @FXML
    private void replaceSelectedItemClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        MenuModel menuItem = menuTable.getSelectionModel().getSelectedItem();
        String name = tfItemName.getText();
        String type = cItemType.getSelectionModel().getSelectedItem();
        float priceValue;

        if (menuItem == null || name.isEmpty() || type == null) {
            return;
        }
        try {
            priceValue = Float.parseFloat(tfPrice.getText());
        } catch (NumberFormatException e) {
            System.out.println("No float in the string.");
            return;
        }

        if (!confirm()) {
            return;
        }

        String sql = "UPDATE menu SET item_name = '"
                   + name + "', item_type = '"
                   + type + "', price = '"
                   + priceValue + "' WHERE id = "
                   + menuItem.getId();
        dbConn.executeUpdate(sql);
        readMenuFromDatabase();
        clearEditMenuInput();
    }

    private boolean confirm() {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Are You Sure?");
        dialog.setResizable(false);
        Optional<ButtonType> type = dialog.showAndWait();
        return type.isPresent() && type.get() == ButtonType.OK;
    }

    @FXML
    private void removeSelectedItemClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        MenuModel menuItem = menuTable.getSelectionModel().getSelectedItem();

        if (menuItem == null) {
            return;
        }
        if (!confirm()) {
            return;
        }

        String sql = "DELETE FROM menu WHERE id = " + menuItem.getId();
        dbConn.executeUpdate(sql);
        readMenuFromDatabase();
    }

    /**
     * **************** Manage Staff Members **********************
     */
    public void readStaffMembersFromDatabase() {
        ObservableList<StaffModel> staffData = null;
        try {
            DBConnection dbConn = mainUI.getDBConnection();
            ResultSet result = dbConn.executeQuery("SELECT * FROM staff_members ORDER BY isManager DESC");
            staffData = FXCollections.observableArrayList();
            while (result.next()) {
                staffData.add(new StaffModel(result.getInt("id"),
                                             result.getString("user_name"),
                                             result.getString("password"),
                                             result.getInt("isManager"),
                                             result.getString("last_login")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        staffTable.setItems(staffData);
    }

    private void clearUserInput() {
        tfUserName.setText("");
        tfPassword.setText("");
    }

    @FXML
    private void addUserClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        String userName = tfUserName.getText();
        String password = tfPassword.getText();
        String staffType = cbStaffType.getSelectionModel().getSelectedItem();

        if (userName.isEmpty() || password.isEmpty() || isManager == null) {
            return;
        }

        if (dbConn.alreadyExists("SELECT id FROM staff_members WHERE user_name = '" + userName + "'")) {
            return;
        }

        int staffTypeNum = 0; //Cashier
        if (staffType.equals("Manager")) {
            staffTypeNum = 1;
        } else if (staffType.equals("Waiter")) {
            staffTypeNum = 2;
        }

        String sql = "INSERT INTO staff_members (user_name,password,isManager) VALUES ('"
                   + userName + "', '"
                   + password + "', "
                   + staffTypeNum + ")"; //0 removed
        dbConn.executeUpdate(sql);

        if (staffTypeNum == 2) { // if waiter
            sql = "INSERT INTO waiters VALUES (0, '" + userName + "', '', now())";
            dbConn.executeUpdate(sql);
        }

        readStaffMembersFromDatabase();
        clearUserInput();
    }

    @FXML
    private void replaceUserClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        String userName = tfUserName.getText();
        String password = tfPassword.getText();
        StaffModel staffMember = staffTable.getSelectionModel().getSelectedItem();
        String isManager = cbStaffType.getValue();

        if (userName.isEmpty() || password.isEmpty() || staffMember == null || isManager == null) {
            return;
        }
        if (!confirm()) {
            return;
        }

        String sql = "UPDATE staff_members SET user_name = '"
                + userName + "', password = '"
                + password + "', isManager = "
                + isManager.equals("Yes") + " WHERE id = "
                + staffMember.getID();
        dbConn.executeUpdate(sql);
        readStaffMembersFromDatabase();
        clearUserInput();
    }

    @FXML
    private void removeUserClicked(ActionEvent event) {
        DBConnection dbConn = mainUI.getDBConnection();
        StaffModel staffMember = staffTable.getSelectionModel().getSelectedItem();

        if (staffMember == null) {
            return;
        }

        if (!confirm()) {
            return;
        }

        String sql = "DELETE FROM staff_members WHERE id = " + staffMember.getID();
        dbConn.executeUpdate(sql);
        readStaffMembersFromDatabase();
        clearUserInput();
    }

    /**
     * ******************* Orders List Controller **********************
     */
    private void showDetails() {
        OrderModel orderModel = (OrderModel) ordersTable.getSelectionModel().getSelectedItem();
        if (orderModel == null) {
            return;
        }

        mainUI.showDetails(orderModel.getOrderID(), orderModel.getDate(), orderModel.getTime());
    }

    private void populateTableNumberAndItems() {
        DBConnection dbConn = mainUI.getDBConnection();
        cTableNumber.getItems().retainAll();
        cItems.getItems().retainAll();

        //0 for ignore table number
        for (int i = 0; i <= BillingSystem.tableNumbers; i++) {
            cTableNumber.getItems().add(i);
        }

        cItems.getItems().add("------");
        cbWaiterName.getItems().add("------");
        cbCashierName.getItems().add("------");

        String sqlMenuItems = "SELECT item_name FROM menu";
        String sqlMinDate = "SELECT min(date) as min_date FROM orders_list";
        String sqlWaiterNames = "SELECT first_name FROM waiters";
        String sqlCashierNames = "SELECT user_name FROM staff_members WHERE isManager = false";

        ResultSet resultMenuItems = dbConn.executeQuery(sqlMenuItems);
        ResultSet resultMinDate = dbConn.executeQuery(sqlMinDate);
        ResultSet resultWaiters = dbConn.executeQuery(sqlWaiterNames);
        ResultSet resultCashiers = dbConn.executeQuery(sqlCashierNames);

        try {
            if (resultMinDate.next()) {
                dpFromDate.setValue(LocalDate.parse(resultMinDate.getString("min_date")));
            } else {
                dpFromDate.setValue(LocalDate.ofEpochDay(0));
            }

            while (resultMenuItems.next()) {
                cItems.getItems().add(resultMenuItems.getString("item_name"));
            }

            while (resultWaiters.next()) {
                cbWaiterName.getItems().add(resultWaiters.getString("first_name"));
            }

            while (resultCashiers.next()) {
                cbCashierName.getItems().add(resultCashiers.getString("user_name"));
            }
        } catch (SQLException e) {
            System.out.println("error while reading menu from db.");
        }

        dpToDate.setValue(LocalDate.now());
    }

    public void readOrdersListFromDatabase() {
        DBConnection dbConn = mainUI.getDBConnection();
        ObservableList<OrderModel> orderData = FXCollections.observableArrayList();

        Integer tableNum = cTableNumber.getSelectionModel().getSelectedItem();
        String item = cItems.getSelectionModel().getSelectedItem();
        String cashierName = cbCashierName.getSelectionModel().getSelectedItem();
        String waiterName = cbWaiterName.getSelectionModel().getSelectedItem();
        String fromDate, toDate;

        String whereClause = "true";

        if (dpFromDate != null && dpToDate != null) {
            fromDate = dpFromDate.getValue().getYear() + "-"
                     + dpFromDate.getValue().getMonthValue() + "-"
                     + dpFromDate.getValue().getDayOfMonth();
            toDate = dpToDate.getValue().getYear() + "-"
                   + dpToDate.getValue().getMonthValue() + "-"
                   + dpToDate.getValue().getDayOfMonth();

            whereClause += " AND orders_list.date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }

        if (tableNum == null || tableNum == 0) {
            whereClause += " AND table_number > 0";
        } else {
            whereClause += " AND table_number = " + tableNum;
        }

        if (waiterName != null && !waiterName.equals("------")) {
            whereClause += " AND first_name = '" + waiterName + "'";
        }

        if (cashierName != null && !cashierName.equals("------")) {
            whereClause += " AND user_name = '" + cashierName + "'";
        }

        String sql;
        if (item == null || item.equals("------")) {
            sql = "SELECT orders_list.order_id, orders_list.table_number, orders_list.date, orders_list.time, staff_members.user_name, orders_list.total_price, CONCAT(waiters.first_name,' ',waiters.last_name) as waiter_name FROM orders_list, staff_members, waiters WHERE "
                    + whereClause
                    + " AND staff_members.id = orders_list.staff_member_id AND orders_list.waiter_id = waiters.id";
        } else {
            whereClause += " AND item_name LIKE '%" + item + "%'";
            sql = "SELECT orders_list.order_id, orders_list.table_number, orders_list.date, orders_list.time, staff_members.user_name, orders_list.total_price, CONCAT(waiters.first_name,' ',waiters.last_name) as waiter_name FROM orders_list, staff_members, individual_orders, menu, waiters WHERE "
                    + whereClause
                    + " AND staff_members.id = orders_list.staff_member_id AND orders_list.order_id = individual_orders.order_id AND individual_orders.item_id = menu.id AND orders_list.waiter_id = waiters.id";
        }

        try {
            ResultSet result = dbConn.executeQuery(sql);
            while (result.next()) {
                orderData.add(new OrderModel(result.getInt("order_id"), result.getInt("table_number"), result.getString("date"), result.getString("time"), result.getFloat("total_price"), result.getString("waiter_name"), result.getString("user_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (generateReport) {
            generateReport = false;
            sql = "SELECT CONCAT('Order ID ', orders_list.order_id), CONCAT('Table ', orders_list.table_number), menu.item_name, menu.item_type, CONCAT(individual_orders.quantity, ' times'), CONCAT(individual_orders.total_price, ' BIRR'), CONCAT(orders_list.total_price, ' BIRR TOTAL'), orders_list.date, orders_list.time, staff_members.user_name FROM orders_list, staff_members, individual_orders, menu, waiters WHERE "
                    + whereClause
                    + " AND staff_members.id = orders_list.staff_member_id AND orders_list.order_id = individual_orders.order_id AND individual_orders.item_id = menu.id";
            String sqlToOutFile = sql + " INTO OUTFILE '" + fullPath.replace('\\', '/') + "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n'";

            dbConn.executeQuery(sqlToOutFile, true);
            try {
                Runtime.getRuntime().exec("explorer " + fullPath);
            } catch (IOException e) {
            }
        }

        ordersTable.setItems(orderData);
    }

    @FXML
    private void searchClicked(ActionEvent event) {
        readOrdersListFromDatabase();
    }

    @FXML
    private void logout(ActionEvent event) {
        manageSelectedFirstTime = true;
        ordersSelectedFirstTime = true;
        mainUI.switchToLogIn();
    }

    @FXML
    private void showDetailsOfSelectedOrder(ActionEvent event) {
        showDetails();
    }

    @FXML
    private void generateReport(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Generate Report");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Compatible", "*.csv"));
        File selectedFile = fc.showSaveDialog(mainUI.getMainStage());
        if (selectedFile != null) {
            generateReport = true; //set flag
            fullPath = selectedFile.getAbsolutePath();
            if (selectedFile.exists()) {
                selectedFile.delete();
            }
            readOrdersListFromDatabase();
        }
    }

    @FXML
    private void manageTabSelected(Event event) {
        if (manageSelectedFirstTime) {
            readStaffMembersFromDatabase();
            manageSelectedFirstTime = false;
        }
    }

    @FXML
    private void ordersTabSelected(Event event) {
        if (ordersSelectedFirstTime) {
            populateTableNumberAndItems();
            readOrdersListFromDatabase();
            ordersSelectedFirstTime = false;
        }
    }

    @FXML
    private void modifyTotalTables(ActionEvent event) {
        try {
            int totalTables = Integer.parseInt(tfTotalTables.getText());
            DBConnection dbConn = mainUI.getDBConnection();
            if (dbConn.executeUpdate("UPDATE number_of_tables SET total = " + totalTables)) {
                BillingSystem.tableNumbers = totalTables;
                tfTotalTables.setPromptText(totalTables + "");
                tfTotalTables.setText("");
            }
        } catch (NumberFormatException e) {
            return;
        }
    }

}
