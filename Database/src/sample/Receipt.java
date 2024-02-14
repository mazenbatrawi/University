package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Receipt implements Initializable {

    private String dbURL;
    private String dbUsername = "";
    private String dbPassword = "";
    private String URL = "";
    private String port = "";
    private String dbName = "";
    private Connection con;
    String query = null;

    @FXML
    private Label bill_id;

    @FXML
    private TableColumn<Bill_Product, Double> cost2;

    @FXML
    private TableColumn<Bill_Product, String> product_name;

    @FXML
    private Label date2;

    @FXML
    private Label employee_id;

    @FXML
    private TableColumn<Bill_Product, String> product_id;

    @FXML
    private TableColumn<Bill_Product, Integer> quantity;

    @FXML
    private TableView<Bill_Product> table2;

    @FXML
    private Label total_cost;

    ObservableList<Bill_Product> list2 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        product_id.setCellValueFactory(new PropertyValueFactory<Bill_Product, String>("product_id"));
        product_name.setCellValueFactory(new PropertyValueFactory<Bill_Product, String>("product_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Bill_Product, Integer>("quantity"));
        cost2.setCellValueFactory(new PropertyValueFactory<Bill_Product, Double>("cost"));
        table2.setItems(null);

        bill_id.setText(BillInterface.bill_id2);
        date2.setText(BillInterface.date2);
        try {
            connectDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        query = "select product_id, quantity, cost from bill_product where bill_id = " + BillInterface.bill_id2;
        String emp = null;

        Statement statement = null;
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            assert statement != null;
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        double total = 0;
        while (true){
            try {
                assert resultSet != null;
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Bill_Product bill_product = new Bill_Product();

            try {
                query = "select product_name from product where barcode = " + resultSet.getString(1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Statement statement1 = null;
            try {
                statement1 = con.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            ResultSet resultSet1 = null;
            try {
                resultSet1 = statement1.executeQuery(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while(true){
                try {
                    if (!resultSet1.next()) break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    bill_product.setProduct_name(resultSet1.getString(1));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                bill_product.setProduct_id(resultSet.getString(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                bill_product.setCost(Double.parseDouble(resultSet.getString(3)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                bill_product.setQuantity(Integer.parseInt(resultSet.getString(2)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            list2.add(bill_product);
            total += bill_product.getQuantity() * bill_product.getCost();
        }
        total_cost.setText(Double.toString(total));
        table2.setItems(list2);

        query = "select emp_name from employee where id = " + BillInterface.emp_id2;
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                emp = resultSet.getString(1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        employee_id.setText(emp);
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void connectDB() throws ClassNotFoundException, SQLException {
        dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
        Properties p = new Properties();
        p.setProperty("user", dbUsername);
        p.setProperty("password", dbPassword);
        p.setProperty("useSSL", "false");
        p.setProperty("autoReconnect", "true");
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection (dbURL, p);
    }
}
