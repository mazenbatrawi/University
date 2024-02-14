package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.util.*;

class P {
    Product p;
    P(Product p) {
        this.p = p;
    }

    public Product getProduct(){
        return p;
    }

    public String get(){
        return p.getBarcode();
    }
}

class sort implements Comparator<P> {
    @Override
    public int compare(P a, P b){
        BigInteger b1 = new BigInteger(a.get());
        BigInteger b2 = new BigInteger(b.get());
        return b1.compareTo(b2);
    }
}

public class ProductInterface implements Initializable {

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

    @FXML
    private TableColumn<Product, String> barcode;

    @FXML
    private TableColumn<Product, String> company_id;

    @FXML
    private TableColumn<Product, String> expire_date;

    @FXML
    private TableColumn<Product, String> name;

    @FXML
    private TableColumn<Product, Double> price;

    @FXML
    private TableColumn<Product, String> production_date;

    @FXML
    private TableColumn<Product, Integer> quantity;

    @FXML
    private TableColumn<Product, String> section_id;

    @FXML
    private TableView<Product> table;

    @FXML
    private ImageView add_image;

    @FXML
    private ImageView queries_image;

    @FXML
    private ImageView search_image;

    @FXML
    private ImageView update_image;

    @FXML
    private TableColumn<Product, Double> selling_price;

    ObservableList<Product> list = FXCollections.observableArrayList();

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
    void add(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("InsertProductController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add New Products");
        stage. setResizable(false);
        stage.show();
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
    void search(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SearchProductController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search Products");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void something(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ProductQueries.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Additional Queries");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("UpdateProductController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Update Products");
        stage. setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readData();
        barcode.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        name.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        company_id.setCellValueFactory(new PropertyValueFactory<Product,String>("company_id"));
        section_id.setCellValueFactory(new PropertyValueFactory<Product,String>("section_id"));
        expire_date.setCellValueFactory(new PropertyValueFactory<Product,String>("expire_date"));
        production_date.setCellValueFactory(new PropertyValueFactory<Product,String>("production_date"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("total_quantity"));
        selling_price.setCellValueFactory(new PropertyValueFactory<Product, Double>("selling_price"));
        table.setItems(list);
    }

    private void readData() {
        ArrayList<P> arrayList = new ArrayList<>();
        try {
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product");
            while(resultSet.next()){
                Product product = new Product(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        Double.parseDouble(resultSet.getString(5)),
                        Integer.parseInt(resultSet.getString(6)),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        Double.parseDouble(resultSet.getString(9))
                );
                arrayList.add(new P(product));
            }
            arrayList.sort(new sort());
            for(P p : arrayList){
                Product product = p.getProduct();
                list.add(product);
            }
            table.setItems(list);
            con.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
