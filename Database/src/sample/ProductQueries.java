package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ProductQueries {

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

    ObservableList<Product> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> table;

    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private RadioButton rb3;

    @FXML
    private RadioButton rb4;

    @FXML
    private TextField value;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView exec_image;

    static Stage stage2;

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

    @FXML
    void run(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(rb1.isSelected()){
            query = "select avg(selling_price) from product";
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            double avg = 0;
            while(resultSet.next()){
                avg = resultSet.getDouble(1);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Average Price");
            alert.setContentText("Average price of products is " + avg);
            alert.showAndWait();
            con.close();
        }
        if(rb2.isSelected()){
            query = "select max(selling_price) from product";
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            double max = 0;
            while(resultSet.next()){
                max = resultSet.getDouble(1);
            }
            query = "select min(selling_price) from product";
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            double min = 0;
            while(resultSet.next()){
                min = resultSet.getDouble(1);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Maximum and minimum prices");
            alert.setContentText("The maximum price is " + max + " and the minimum price is " + min);
            alert.showAndWait();
            con.close();
        }
        if(rb3.isSelected()){
            if(value.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Give a value for the field");
                alert.showAndWait();
            }
            else{
                double val = Double.parseDouble(value.getText());
                query = "select product_name, selling_price from product where selling_price >= " + val;
                connectDB();
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                table.getItems().clear();
                table.getColumns().clear();

                TableColumn<Product, String> column = new TableColumn<>();
                column.setText("Name");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
                table.getColumns().add(column);

                TableColumn<Product, String> column2 = new TableColumn<>();
                column2.setText("Selling Price");
                column2.setMinWidth(130);
                column2.setCellValueFactory(new PropertyValueFactory<Product, String>("selling_price"));
                table.getColumns().add(column2);

                while(resultSet.next()) {
                    Product product = new Product();
                    product.setName(resultSet.getString(1));
                    product.setSelling_price(resultSet.getDouble(2));
                    list.add(product);
                }
                table.setItems(list);
                con.close();
            }
        }
        if(rb4.isSelected()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Expire.fxml"));
            Parent root2 = loader.load();
            Scene scene2 = new Scene(root2);
            stage2 = new Stage();
            stage2.setScene(scene2);
            stage2.setTitle("Products with expired dates");
            stage2. setResizable(false);
            stage2.show();
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
