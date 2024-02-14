package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class SearchBill {

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
    private CheckBox bill_id_button;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView search_image;

    @FXML
    private CheckBox cost_button;

    @FXML
    private CheckBox date_button;

    @FXML
    private CheckBox employee_id_button;

    @FXML
    private TextField sbill_id;

    @FXML
    private TextField scost;

    @FXML
    private DatePicker sdate;

    @FXML
    private TextField semployee_id;

    @FXML
    private TableView<Bill> table;

    ObservableList<Bill> list = FXCollections.observableArrayList();

    @FXML
    void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sample_bill.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Bills");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void search(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!bill_id_button.isSelected() && !employee_id_button.isSelected() && !date_button.isSelected() &&
            !cost_button.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select at least 1 column to show from the check boxes");
            alert.showAndWait();
        }
        else if(sbill_id.getText().isEmpty() && semployee_id.getText().isEmpty() && sdate.getValue() == null &&
                scost.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your search criterion");
            alert.showAndWait();
        }
        else{
            query = "select ";
            if (bill_id_button.isSelected()) query += "id, ";
            if (employee_id_button.isSelected()) query += "employee_id, ";
            if (date_button.isSelected()) query += "datee, ";
            if (cost_button.isSelected()) query += "cost, ";

            query = query.substring(0, query.length() - 2) + " from bill where ";
            if (!sbill_id.getText().isEmpty()) query += "id = " + sbill_id.getText() + " and ";
            if (!semployee_id.getText().isEmpty()) query += "employee_id = " + semployee_id.getText() + " and ";
            if (sdate.getValue() != null) query += "datee = \"" + sdate.getValue().toString() + "\" and ";
            if (!scost.getText().isEmpty()) query += "cost = \"" + scost.getText() + "\" and ";

            query = query.substring(0, query.length() - 5);

            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<String> arrayList = new ArrayList<>();

            table.getItems().clear();
            table.getColumns().clear();

            if (bill_id_button.isSelected()) {
                TableColumn<Bill, String> column = new TableColumn<>();
                column.setText("Bill ID");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Bill, String>("id"));
                table.getColumns().add(column);
                arrayList.add("id");
            }
            if (employee_id_button.isSelected()) {
                TableColumn<Bill, String> column = new TableColumn<>();
                column.setText("Employee ID");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Bill, String>("emp_id"));
                table.getColumns().add(column);
                arrayList.add("emp_id");
            }
            if (date_button.isSelected()) {
                TableColumn<Bill, String> column = new TableColumn<>();
                column.setText("Date");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Bill, String>("date"));
                table.getColumns().add(column);
                arrayList.add("date");
            }
            if (cost_button.isSelected()) {
                TableColumn<Bill, Double> column = new TableColumn<>();
                column.setText("Cost");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Bill, Double>("cost"));
                table.getColumns().add(column);
                arrayList.add("cost");
            }
            while (resultSet.next()) {
                Bill bill = new Bill();
                for (int i = 0; i < arrayList.size(); i++) {
                    switch (arrayList.get(i)) {
                        case "id":
                            bill.setId(resultSet.getString(i + 1));
                            break;
                        case "emp_id":
                            bill.setEmp_id(resultSet.getString(i + 1));
                            break;
                        case "date":
                            bill.setDate(resultSet.getString(i + 1));
                            break;
                        case "cost":
                            bill.setCost(Double.parseDouble(resultSet.getString(i + 1)));
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
                list.add(bill);
            }

            table.setItems(list);
            con.close();
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
