package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class SectionInterface implements Initializable {
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
    private ImageView add_image;

    @FXML
    private ImageView count_image;

    @FXML
    private ImageView delete_image;

    @FXML
    private ImageView searc_image2;

    @FXML
    private ImageView search_image_1;

    @FXML
    private ImageView update_image;

    @FXML
    private Text answer;

    @FXML
    private TextField count;

    @FXML
    private TableColumn<Section, String> name;

    @FXML
    private TableColumn<Section, String> section_id;

    @FXML
    private TableView<Section> table;

    ObservableList<Section> list = FXCollections.observableArrayList();

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
    void homePage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home Page");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    public void readData(){
        try {
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from section order by section_id asc");
            while(resultSet.next()){
                list.add(new Section(
                        resultSet.getString(1),
                        resultSet.getString(2)
                ));
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void countEMP(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(count.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please insert the section id");
            alert.showAndWait();
        }
        else {
            int id = Integer.parseInt(count.getText());
            query = "select count(*) from employee where section_id = " + id;
            connectDB();

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int cnt = 0;
            while (resultSet.next()) {
                cnt = resultSet.getInt(1);
            }
            answer.setText("There are " + cnt + " employees working in section " + count.getText());
            con.close();
        }
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle rb) {
        readData();
        section_id.setCellValueFactory(new PropertyValueFactory<Section, String>("section_id"));
        name.setCellValueFactory(new PropertyValueFactory<Section, String>("section_name"));
        table.setItems(list);
    }

    @FXML
    public void insertClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("InsertSectionController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add New Sections");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    public void updateClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("UpdateSectionController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Update Sections");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    public void searchClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SearchSectionController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search Sections");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void searchEmp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SearchEmployeesInSectionsController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search Employees In Sections");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void deleteSection(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("DeleteSectionController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Delete Sections");
        stage. setResizable(false);
        stage.show();
    }
}