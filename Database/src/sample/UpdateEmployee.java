package sample;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class UpdateEmployee {

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
    private DatePicker hire_date;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView update_image;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField national_id;

    @FXML
    private TextField phone;

    @FXML
    private TextField salary;

    @FXML
    private TextField section;

    @FXML
    private TextField shift;

    @FXML
    private DatePicker shire_date;

    @FXML
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private TextField snational_id;

    @FXML
    private TextField sphone;

    @FXML
    private TextField ssalary;

    @FXML
    private TextField ssection;

    @FXML
    private TextField sshift;

    @FXML
    private TextField password;

    @FXML
    private TextField spassword;

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

    @FXML
    void update(MouseEvent event) throws SQLException, ClassNotFoundException {

        if(sid.getText().isEmpty() && snational_id.getText().isEmpty() && sname.getText().isEmpty() && sphone.getText().isEmpty() &&
            sshift.getText().isEmpty() && ssalary.getText().isEmpty() && ssection.getText().isEmpty() &&
                shire_date.getValue() == null && spassword.getText().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your update criterion");
            alert.showAndWait();
        }

        else if(id.getText().isEmpty() && national_id.getText().isEmpty() && name.getText().isEmpty() && phone.getText().isEmpty() &&
                shift.getText().isEmpty() && salary.getText().isEmpty() && section.getText().isEmpty() &&
                hire_date.getValue() == null && password.getText().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your updates");
            alert.showAndWait();
        }
        else {
            query = "update employee set ";
            if (!id.getText().isEmpty()) query += "id = " + id.getText() + ", ";
            if (!national_id.getText().isEmpty()) query += "national_id = " + "\"" + national_id.getText() + "\", ";
            if (!name.getText().isEmpty()) query += "emp_name = \"" + name.getText() + "\", ";
            if (!phone.getText().isEmpty()) query += "phone = \"" + phone.getText() + "\", ";
            if (!section.getText().isEmpty()) query += "section_id = " + section.getText() + ", ";
            if (!shift.getText().isEmpty()) query += "shift = \"" + shift.getText() + "\", ";
            if (!salary.getText().isEmpty()) query += "salary = " + salary.getText() + ", ";
            if (hire_date.getValue() != null) query += "hire_date = \"" + hire_date.getValue().toString() + "\", ";
            if (!password.getText().isEmpty()) query += "password = \"" + password.getText() + "\", ";

            query = query.substring(0, query.length() - 2) + " where ";

            if (!sid.getText().isEmpty()) query += "id = " + sid.getText() + " and ";
            if (!snational_id.getText().isEmpty()) query += "national_id = \"" + snational_id.getText() + "\" and ";
            if (!sname.getText().isEmpty()) query += "emp_name = \"" + sname.getText() + "\" and ";
            if (!sphone.getText().isEmpty()) query += "phone = \"" + sphone.getText() + "\" and ";
            if (!ssection.getText().isEmpty()) query += "section_id = " + ssection.getText() + " and ";
            if (!sshift.getText().isEmpty()) query += "shift = \"" + sshift.getText() + "\" and ";
            if (!ssalary.getText().isEmpty()) query += "salary = " + ssalary.getText() + " and ";
            if (shire_date.getValue() != null) query += "hire_date = \"" + shire_date.getValue().toString() + "\" and ";
            if (!spassword.getText().isEmpty()) query += "password = \"" + spassword.getText() + "\" and ";

            query = query.substring(0, query.length() - 5);

            try {
                connectDB();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                id.setText(null);
                national_id.setText(null);
                name.setText(null);
                phone.setText(null);
                section.setText(null);
                shift.setText(null);
                salary.setText(null);
                hire_date.getEditor().clear();
                password.setText(null);

                sid.setText(null);
                snational_id.setText(null);
                sname.setText(null);
                sphone.setText(null);
                ssection.setText(null);
                sshift.setText(null);
                ssalary.setText(null);
                shire_date.getEditor().clear();
                spassword.setText(null);

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
