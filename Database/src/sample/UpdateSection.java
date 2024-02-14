package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UpdateSection {

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
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView update_image;

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
    void update(ActionEvent event) {
        if(sid.getText().isEmpty() && sname.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your search criterion");
            alert.showAndWait();
        }

        else if(id.getText().isEmpty() && name.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your updates");
            alert.showAndWait();
        }

        else {
            query = "update Section set ";
            if (!id.getText().isEmpty()) query += "section_id = " + id.getText() + ", ";
            if (!name.getText().isEmpty()) query += "section_name = \"" + name.getText() + "\", ";

            query = query.substring(0, query.length() - 2) + " where ";

            if (!sid.getText().isEmpty()) query += "section_id = " + sid.getText() + " and ";
            if (!sname.getText().isEmpty()) query += "section_name = \"" + sname.getText() + "\" and ";

            query = query.substring(0, query.length() - 5);

            try {
                connectDB();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                id.setText(null);
                name.setText(null);

                sid.setText(null);
                sname.setText(null);

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
