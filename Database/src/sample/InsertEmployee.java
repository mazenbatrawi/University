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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertEmployee implements Initializable {
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
    private ImageView add_image;

    @FXML
    private ImageView back_image;

    @FXML
    private DatePicker hire_date;

    @FXML
    private TextField password;

    @FXML
    private TextField name;

    @FXML
    private TextField national_id;

    @FXML
    private TextField phone;

    @FXML
    private TextField salary;

    @FXML
    private TextField section_id;

    @FXML
    private TextField shift;

    @FXML
    private AnchorPane table;

    String query = null;

    @FXML
    void add(ActionEvent event) throws SQLException, ClassNotFoundException {
        String national_id1, name1, phone1, shift1, password1;
        double salary1;
        int section_id1;
        String hire_date1;

        if (national_id.getText().isEmpty() || name.getText().isEmpty() || phone.getText().isEmpty() || shift.getText().isEmpty() ||
                hire_date.getValue() == null || salary.getText().isEmpty() || section_id.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        }
        else {
            national_id1 = national_id.getText();
            name1 = name.getText();
            phone1 = phone.getText();
            section_id1 = Integer.parseInt(section_id.getText());
            shift1 = shift.getText();
            salary1 = Double.parseDouble(salary.getText());
            hire_date1 = hire_date.getValue().toString();
            password1 = password.getText();

            query = "INSERT INTO employee (national_id, emp_name, phone, section_id, salary, hire_date, shift, password) " +
                    "VALUES (" + "\"" + national_id1 + "\", " +
                    "\"" + name1 + "\", " +
                    "\"" + phone1 + "\", " +
                    section_id1 + ", " +
                    salary1 + ", " +
                    "\"" + hire_date1 + "\", " +
                    "\"" + shift1 + "\", " +
                    "\"" + password1 + "\")";
            try {
                connectDB();

                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                national_id.setText(null);
                name.setText(null);
                phone.setText(null);
                section_id.setText(null);
                shift.setText(null);
                salary.setText(null);
                hire_date.getEditor().clear();
                password.setText(null);

                con.close();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("An error happened, please check your data!");
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void backClick(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Employees");
        stage. setResizable(false);
        stage.show();
    }

    public static void main(String[] args){

    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle rb) {

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
