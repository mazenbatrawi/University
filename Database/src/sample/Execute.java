package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class Execute implements Initializable {

    private String dbURL;
    private String dbUsername = "";
    private String dbPassword = "";
    private String URL = "";
    private String port = "";
    private String dbName = "";
    private Connection con;
    String query = null;

    @FXML
    private TableColumn<Product, String> barcode;

    @FXML
    private TableColumn<Product, String> company_id;

    @FXML
    private TableColumn<Product, String> expire_date;

    @FXML
    private TableColumn<Product, String> name;

    @FXML
    private RadioButton no;

    @FXML
    private TableColumn<Product, Double> price;

    @FXML
    private TableColumn<Product, String> production_date;

    @FXML
    private TableColumn<Product, Integer> quantity;

    @FXML
    private TableColumn<Product, String> section_id;

    @FXML
    private TableColumn<Product, Double> selling_price;

    @FXML
    private TableView<Product> table;

    @FXML
    private ToggleGroup tg;

    @FXML
    private RadioButton yes;

    ArrayList<Product> arrayList = new ArrayList<>();
    ObservableList<Product> list = FXCollections.observableArrayList();

    @FXML
    void remove(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!yes.isSelected() && !no.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select whether you want to delete the expired products or not");
            alert.showAndWait();
        }
        else{
            connectDB();
            if(yes.isSelected()){
                for(Product p : arrayList){
                    query = "update product set total_quantity = 0 where barcode = " + p.getBarcode();
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                }
            }
            con.close();
            ProductQueries.stage2.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barcode.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        name.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        company_id.setCellValueFactory(new PropertyValueFactory<Product,String>("company_id"));
        section_id.setCellValueFactory(new PropertyValueFactory<Product,String>("section_id"));
        expire_date.setCellValueFactory(new PropertyValueFactory<Product,String>("expire_date"));
        production_date.setCellValueFactory(new PropertyValueFactory<Product,String>("production_date"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("total_quantity"));
        selling_price.setCellValueFactory(new PropertyValueFactory<Product, Double>("selling_price"));
        try {
            readData();
        } catch (SQLException | ClassNotFoundException throwables) {
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

    private void readData() throws SQLException, ClassNotFoundException {
        connectDB();
        query = "select * from product";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            String expire = resultSet.getString(8);
            int day = (expire.charAt(8) - '0') * 10 + expire.charAt(9) - '0';
            int month = (expire.charAt(5) - '0') * 10 + expire.charAt(6) - '0';
            int year = (expire.charAt(0) - '0') * 1000 + (expire.charAt(1) - '0') * 100 + (expire.charAt(2) - '0') * 10 +
                    (expire.charAt(3) - '0');
            GregorianCalendar calendar = new GregorianCalendar();
            GregorianCalendar calendar1 = new GregorianCalendar(year, month - 1, day);
            if(calendar.compareTo(calendar1) >= 0){
                Product product = new Product(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        Double.parseDouble(resultSet.getString(5)),
                        Integer.parseInt(resultSet.getString(6)),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        Double.parseDouble(resultSet.getString(9)));
                list.add(product);
                arrayList.add(product);
            }
        }
        table.setItems(list);
        con.close();
    }
}
