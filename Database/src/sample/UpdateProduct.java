package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UpdateProduct {

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
    private ImageView back_image;

    @FXML
    private TextField barcode;

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
    private TextField sbarcode;

    @FXML
    private TextField scompany_id;

    @FXML
    private TextField section_id;

    @FXML
    private TextField selling_price;

    @FXML
    private DatePicker sexpire_date;

    @FXML
    private TextField sname;

    @FXML
    private TextField sprice;

    @FXML
    private DatePicker sproduction_date;

    @FXML
    private TextField squantity;

    @FXML
    private TextField ssection_id;

    @FXML
    private TextField sselling_price;

    @FXML
    private ImageView update_image;

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
    void update(ActionEvent event) {
        if(sbarcode.getText().isEmpty() && sname.getText().isEmpty() && scompany_id.getText().isEmpty() && ssection_id.getText().isEmpty() &&
                sprice.getText().isEmpty() && squantity.getText().isEmpty() && sproduction_date.getValue() == null &&
                sexpire_date.getValue() == null && sselling_price.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your update criterion");
            alert.showAndWait();
        }
        else if(barcode.getText().isEmpty() && name.getText().isEmpty() && company_id.getText().isEmpty() && section_id.getText().isEmpty() &&
                price.getText().isEmpty() && quantity.getText().isEmpty() && production_date.getValue() == null &&
                expire_date.getValue() == null && selling_price.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your new values");
            alert.showAndWait();
        }
        else{
            query = "update product set ";
            if (!barcode.getText().isEmpty()) query += "barcode = \"" + barcode.getText() + "\", ";
            if (!name.getText().isEmpty()) query += "product_name = \"" + name.getText() + "\", ";
            if (!company_id.getText().isEmpty()) query += "company_id = " + company_id.getText() + ", ";
            if (!section_id.getText().isEmpty()) query += "section_id = " + section_id.getText() + ", ";
            if (!price.getText().isEmpty()) query += "price = " + price.getText() + ", ";
            if (!quantity.getText().isEmpty()) query += "total_quantity = " + quantity.getText() + ", ";
            if (production_date.getValue() != null) query += "production_date = \"" + production_date.getValue().toString() + "\", ";
            if (expire_date.getValue() != null) query += "expire_date = \"" + expire_date.getValue().toString() + "\", ";
            if (!selling_price.getText().isEmpty()) query += "selling_price = " + selling_price.getText() + ", ";

            query = query.substring(0, query.length() - 2) + " where ";

            if (!sbarcode.getText().isEmpty()) query += "barcode = \"" + sbarcode.getText() + "\" and ";
            if (!sname.getText().isEmpty()) query += "product_name = \"" + sname.getText() + "\" and ";
            if (!scompany_id.getText().isEmpty()) query += "company_id = " + scompany_id.getText() + " and ";
            if (!ssection_id.getText().isEmpty()) query += "section_id = " + ssection_id.getText() + " and ";
            if (!sprice.getText().isEmpty()) query += "price = " + sprice.getText() + " and ";
            if (!squantity.getText().isEmpty()) query += "total_quantity = " + squantity.getText() + " and ";
            if (sproduction_date.getValue() != null) query += "production_date = '" + sproduction_date.getValue().toString() + "' and ";
            if (sexpire_date.getValue() != null) query += "expire_date = '" + sexpire_date.getValue().toString() + "' and ";
            if (!sselling_price.getText().isEmpty()) query += "selling_price = " + sselling_price.getText() + " and ";

            query = query.substring(0, query.length() - 5);

            try {
                connectDB();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                barcode.setText(null);
                name.setText(null);
                company_id.setText(null);
                expire_date.getEditor().clear();
                section_id.setText(null);
                price.setText(null);
                quantity.setText(null);
                production_date.getEditor().clear();
                selling_price.setText(null);

                sbarcode.setText(null);
                sname.setText(null);
                scompany_id.setText(null);
                ssection_id.setText(null);
                sprice.setText(null);
                squantity.setText(null);
                sproduction_date.getEditor().clear();
                sexpire_date.getEditor().clear();
                sselling_price.setText(null);

                con.close();
            }
            catch (SQLException | ClassNotFoundException ex) {
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
