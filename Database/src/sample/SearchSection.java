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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

public class SearchSection {
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
    private CheckBox id_button;

    @FXML
    private CheckBox name_button;

    @FXML
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView search_image;

    @FXML
    private TableView<Section> table;

    ObservableList<Section> list = FXCollections.observableArrayList();

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
    void searchClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(sid.getText().isEmpty() && sname.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your search criterion");
            alert.showAndWait();
        }

        else if(!id_button.isSelected() && !name_button.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select at least 1 column to show from the check boxes");
            alert.showAndWait();
        }
        else{
            query = "select ";
            if(id_button.isSelected()) query += "section_id, ";
            if(name_button.isSelected()) query += "section_name, ";

            query = query.substring(0, query.length() - 2) + " from Section where ";

            if(!sid.getText().isEmpty()) query += "section_id = " + sid.getText() + " and ";
            if(!sname.getText().isEmpty()) query += "section_name = \"" + sname.getText() + "\" and ";

            query = query.substring(0, query.length() - 5);

            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<String> arrayList = new ArrayList<>();

            table.getItems().clear();
            table.getColumns().clear();

            if(id_button.isSelected()) {addColumn("ID", "section_id"); arrayList.add("id");}
            if(name_button.isSelected()) {addColumn("Name", "section_name"); arrayList.add("name");}

            while(resultSet.next()){
                Section section = new Section();
                for(int i = 0; i < arrayList.size(); i++){
                    switch (arrayList.get(i)) {
                        case "id":
                            section.setSection_id(resultSet.getString(i + 1));
                            break;
                        case "name":
                            section.setSection_name(resultSet.getString(i + 1));
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
                list.add(section);
            }

            table.setItems(list);
            con.close();
        }
    }

    @FXML
    private void addColumn(String Name, String name) {
        TableColumn<Section, String> column = new TableColumn<>();
        column.setText(Name);
        column.setMinWidth(130);
        column.setCellValueFactory(new PropertyValueFactory<Section, String>(name));
        table.getColumns().add(column);
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