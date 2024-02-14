package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class InsertSection implements Initializable{
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
    private TextField name;

    @FXML
    private AnchorPane table;

    @FXML
    private ImageView add_image;

    @FXML
    private ImageView back_image;

    @FXML
    void add(ActionEvent event) {
        if(name.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        }
        else {
            query = "INSERT INTO Section (section_name) " + "VALUES (" + "?" + ")";
            try {
                connectDB();
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, name.getText());
                statement.executeUpdate();
                name.setText(null);
                con.close();
            } catch (SQLException | ClassNotFoundException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("An error happened, please check your data!");
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void backClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sample_section.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sections");
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
