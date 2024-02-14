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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class DeleteSection implements Initializable {

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
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView delete_image;

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

    @FXML
    void delete(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(sid.getText().isEmpty() && sname.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one of the delete criterion");
            alert.showAndWait();
        }
        else {

            query = "delete from section where ";
            boolean ok = false;
            if (!sid.getText().isEmpty()) {query += "section_id = " + "?" + " and "; ok = true;}
            if (!sname.getText().isEmpty()) query += "section_name = " + "?" + " and ";

            query = query.substring(0, query.length() - 5);

            connectDB();

            PreparedStatement statement = con.prepareStatement(query);
            if (!sid.getText().isEmpty()) statement.setString(1, sid.getText());
            if (!sname.getText().isEmpty()) statement.setString(1 + (ok ? 1 : 0), sname.getText());
            statement.executeUpdate();
            con.close();
            sid.setText(null);
            sname.setText(null);
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

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

    }
}
