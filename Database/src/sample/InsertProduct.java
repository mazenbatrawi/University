package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class InsertProduct implements Initializable {

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
    private TextField barcode;

    @FXML
    private TextField selling_price;

    @FXML
    private TextField company_id;

    @FXML
    private DatePicker expire_date;

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private DatePicker production_date;

    @FXML
    private TextField quantity;

    @FXML
    private TextField section_id;

    @FXML
    private AnchorPane table;

    @FXML
    private ImageView add_image;

    @FXML
    private ImageView back_image;

    @FXML
    void insert(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(barcode.getText().isEmpty() || company_id.getText().isEmpty() || expire_date.getValue() == null ||
            name.getText().isEmpty() || price.getText().isEmpty() || production_date.getValue() == null ||
            quantity.getText().isEmpty() || section_id.getText().isEmpty() || selling_price.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        }
        else{
            String code = barcode.getText();
            query = "select count(*) from product where barcode = '" + code + "'";
            connectDB();

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int cnt = 0;
            while(resultSet.next()){
                cnt = resultSet.getInt(1);
            }
            if(cnt != 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("There is a product with barcode " + code + " in the table");
                alert.showAndWait();
            }
            else {
                query = "insert into product values (" +
                        "'" + barcode.getText() + "', " +
                        "'" + name.getText() + "', " +
                               company_id.getText() + ", " +
                               section_id.getText() + ", " +
                               price.getText() + ", " +
                               quantity.getText() + ", " +
                        "'" + production_date.getValue().toString() + "', " +
                        "'" + expire_date.getValue().toString() + "', " +
                               selling_price.getText() + ")";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                barcode.setText(null);
                name.setText(null);
                company_id.setText(null);
                section_id.setText(null);
                price.setText(null);
                quantity.setText(null);
                production_date.getEditor().clear();
                expire_date.getEditor().clear();
                selling_price.setText(null);

                con.close();
            }
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sample_product.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Products");
        stage. setResizable(false);
        stage.show();
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

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

    }
}
