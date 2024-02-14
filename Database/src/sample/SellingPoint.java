package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;

public class SellingPoint implements Initializable{

    private String dbURL;
    private String dbUsername = "";
    private String dbPassword = "";
    private String URL = "";
    private String port = "";
    private String dbName = "";
    private Connection con;
    private Stage stage;
    private Scene scene;
    private Parent root;
    String query = null;

    @FXML
    private ImageView add_image;

    @FXML
    private ImageView clear_image;

    @FXML
    private DatePicker date;

    @FXML
    private TextField id;

    @FXML
    private TextField paid;

    @FXML
    private TableColumn<Bill_Product, Double> product_cost;

    @FXML
    private TableColumn<Bill_Product, String> product_id;

    @FXML
    private TableColumn<Bill_Product, Integer> product_quantity;

    @FXML
    private TextField quantity;

    @FXML
    private ImageView remove_image;

    @FXML
    private ImageView sell_image;

    @FXML
    private TableView<Bill_Product> table;

    @FXML
    private Label total;

    @FXML
    void remove(ActionEvent event) throws SQLException, ClassNotFoundException {
        int quant = table.getSelectionModel().getSelectedItem().getQuantity();
        String barcode = table.getSelectionModel().getSelectedItem().getProduct_id();
        table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
        double cost = 0;
        for(int i = 0; i < table.getItems().size(); i++){
            Bill_Product bill_product = table.getItems().get(i);
            cost += bill_product.getQuantity() * bill_product.getCost();
        }
        total.setText(Double.toString(cost));
        query = "update product set total_quantity = total_quantity + " + quant + " where barcode = \"" + barcode + "\"";
        connectDB();
        Statement statement = con.createStatement();
        statement.executeUpdate(query);
        con.close();
    }

    @FXML
    void sell(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(table.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Add some products to the bill");
            alert.showAndWait();
        }
        else if(date.getValue() == null || paid.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Enter your ID and your password and the date and make sure the paid amount is greater than the cost");
            alert.showAndWait();
        }
        else{
            connectDB();

            double cost = 0;
            for(int i = 0; i < table.getItems().size(); i++){
                Bill_Product bill_product = table.getItems().get(i);
                cost += bill_product.getQuantity() * bill_product.getCost();
            }
            total.setText(Double.toString(cost));
            if(Double.parseDouble(paid.getText()) >= cost) {
                query = "insert into bill (employee_id, datee, cost) values(\"" +
                        Login.empID + "\", \"" + date.getValue().toString() + "\"" +
                        ", " + cost + ")";

                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                query = "SELECT id FROM bill WHERE id = (SELECT max(id) FROM bill)";
                statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                int bid = -1;
                while (resultSet.next()) {
                    bid = resultSet.getInt(1);
                }
                HashMap<String, Integer> map = new HashMap<>();
                HashMap<String, Double> map2 = new HashMap<>();
                for (int i = 0; i < table.getItems().size(); i++) {
                    Bill_Product bill_product = table.getItems().get(i);
                    if (map.containsKey(bill_product.getProduct_id())) {
                        int count = map.get(bill_product.getProduct_id());
                        count += bill_product.getQuantity();
                        map.put(bill_product.getProduct_id(), count);
                    } else {
                        map.put(bill_product.getProduct_id(), bill_product.getQuantity());
                    }
                    if (!map2.containsKey(bill_product.getProduct_id())) {
                        map2.put(bill_product.getProduct_id(), bill_product.getCost());
                    }
                }
                for (Entry<String, Integer> stringIntegerEntry : map.entrySet()) {

                    query = "insert into Bill_Product (bill_id, product_id, quantity, cost) values(" +
                            bid + ", \"" + ((Entry) stringIntegerEntry).getKey() + "\", " + ((Entry) stringIntegerEntry).getValue() + ", " + map2.get(((Entry) stringIntegerEntry).getKey()) + ")";
                    statement = con.createStatement();
                    statement.executeUpdate(query);
                }
                id.setText(null);
                quantity.setText(null);
                total.setText("0.0");
                date.getEditor().clear();
                table.setItems(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Change = " + ((Double.parseDouble(paid.getText())) - cost));
                paid.setText(null);
                alert.showAndWait();
                con.close();
                root = FXMLLoader.load(getClass().getResource("SellingPoint.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Selling Point");
                stage.setResizable(false);
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("The paid amount is less than the cost");
                alert.showAndWait();
            }
            con.close();
        }
    }

    @FXML
    void clear(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        connectDB();
        for(int i = 0; i < table.getItems().size(); i++){
            Bill_Product bill_product = table.getItems().get(i);
            query = "update product set total_quantity = total_quantity + " + bill_product.getQuantity() + " where barcode = \"" + bill_product.getProduct_id() + "\"";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        }
        con.close();
        root = FXMLLoader.load(getClass().getResource("SellingPoint.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Selling Point");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void add(ActionEvent event) throws SQLException, ClassNotFoundException {
        product_cost.setCellValueFactory(new PropertyValueFactory<Bill_Product, Double>("cost"));
        product_id.setCellValueFactory(new PropertyValueFactory<Bill_Product, String>("product_id"));
        product_quantity.setCellValueFactory(new PropertyValueFactory<Bill_Product, Integer>("quantity"));

        if(id.getText().isEmpty() || quantity.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Enter the product's barcode and it's quantity to add it to the bill!");
            alert.showAndWait();
        }
        else{
            String barcode = id.getText();
            int quant = Integer.parseInt(quantity.getText());
            double price = 0;
            int av = 0;

            query = "select selling_price, total_quantity from product where barcode = \"" + barcode + "\"";
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int results = 0;
            while(resultSet.next()){
                results++;
                price = resultSet.getDouble(1);
                av = resultSet.getInt(2);
            }
            if(results == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("No such product");
                alert.showAndWait();
            }
            else if(av < quant){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("The available amount from the product is " + av);
                alert.showAndWait();
            }
            else{
                statement = con.createStatement();
                resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    Bill_Product bill_product = new Bill_Product();
                    bill_product.setProduct_id(barcode);
                    bill_product.setQuantity(quant);
                    bill_product.setCost(price);
                    table.getItems().add(bill_product);
                }
                double cost = 0;
                for(int i = 0; i < table.getItems().size(); i++){
                    Bill_Product bill_product = table.getItems().get(i);
                    cost += bill_product.getQuantity() * bill_product.getCost();
                }
                total.setText(Double.toString(cost));
                query = "update product set total_quantity = total_quantity - " + quant + " where barcode = \"" + barcode + "\"";
                statement = con.createStatement();
                statement.executeUpdate(query);
            }
            con.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        product_cost.setCellValueFactory(new PropertyValueFactory<Bill_Product, Double>("cost"));
        product_id.setCellValueFactory(new PropertyValueFactory<Bill_Product, String>("product_id"));
        product_quantity.setCellValueFactory(new PropertyValueFactory<Bill_Product, Integer>("quantity"));
        total.setText("0.0");
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

    @FXML
    void homePage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home Page");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        if(table.getItems().isEmpty()){
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage. setResizable(false);
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Sell The Order Before Logging Out Or Clear It");
            alert.showAndWait();
        }
    }
}
