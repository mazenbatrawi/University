package sample;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Login implements Initializable {

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
    static String empID = null;

    @FXML
    private ImageView location;

    @FXML
    private ImageView owner;

    @FXML
    private ImageView logo;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView phone;

    @FXML
    private TextField username;

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
    void click(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(username.getText().isEmpty() || password.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Enter your ID and your Password to continue");
            alert.showAndWait();
        }
        else{
            if(username.getText().equals("admin") && password.getText().equals("admin")){
                try {
                    root = FXMLLoader.load(getClass().getResource("main.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setTitle("Admin");
                stage.setScene(scene);
                stage. setResizable(false);
                stage.show();
            }
            else{
                boolean isNumeric = username.getText().chars().allMatch( Character::isDigit );
                if(!isNumeric){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Enter a correct ID and Password");
                    alert.showAndWait();
                }
                else {
                    query = "select * from Employee where id = " + username.getText() + " and password = " + password.getText();

                    connectDB();
                    Statement statement = con.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    int cnt = 0;
                    while (resultSet.next()) {
                        empID = resultSet.getString(1);
                        cnt++;
                    }
                    if (cnt == 1) {
                        root = FXMLLoader.load(getClass().getResource("SellingPoint.fxml"));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Selling Point");
                        stage.setResizable(false);
                        stage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Enter a correct ID and Password");
                        alert.showAndWait();
                    }
                    con.close();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
