package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private RadioButton bill_interface;

    @FXML
    private ImageView location;

    @FXML
    private ImageView logo;

    @FXML
    private ImageView owner;

    @FXML
    private ImageView phone;

    @FXML
    private RadioButton emp_interface;

    @FXML
    private RadioButton prod_interface;

    @FXML
    private RadioButton sec_interface;

    @FXML
    private ToggleGroup tg;

    @FXML
    void chooseInterface(ActionEvent event) throws IOException {
        if(emp_interface.isSelected()){
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Employees");
            stage.setScene(scene);
            stage. setResizable(false);
            stage.show();
        }
        else if(sec_interface.isSelected()){
            root = FXMLLoader.load(getClass().getResource("sample_section.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sections");
            stage. setResizable(false);
            stage.show();
        }
        else if(prod_interface.isSelected()){
            root = FXMLLoader.load(getClass().getResource("sample_product.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Products");
            stage. setResizable(false);
            stage.show();
        }
        else if(bill_interface.isSelected()){
            root = FXMLLoader.load(getClass().getResource("sample_bill.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Bills");
            stage. setResizable(false);
            stage.show();
        }
    }

    @FXML
    void stats(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Statistics");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage. setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
