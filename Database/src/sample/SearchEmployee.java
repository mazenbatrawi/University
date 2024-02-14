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

public class SearchEmployee {
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
    private CheckBox hire_date_button;

    @FXML
    private CheckBox password_button;

    @FXML
    private CheckBox id_button;

    @FXML
    private CheckBox name_button;

    @FXML
    private CheckBox national_id_button;

    @FXML
    private CheckBox phone_button;

    @FXML
    private CheckBox salary_button;

    @FXML
    private CheckBox section_button;

    @FXML
    private CheckBox shift_button;

    @FXML
    private DatePicker shire_date;

    @FXML
    private TextField spassword;

    @FXML
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private TextField snational_id;

    @FXML
    private TextField sphone;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView search_image;

    @FXML
    private TextField ssalary;

    @FXML
    private TextField ssection;

    @FXML
    private TextField sshift;

    @FXML
    private TableView<Employee> table;

    ObservableList<Employee> list = FXCollections.observableArrayList();

    @FXML
    void searchClick(ActionEvent event) throws SQLException, ClassNotFoundException{

        if(sid.getText().isEmpty() && snational_id.getText().isEmpty() && sname.getText().isEmpty() && sphone.getText().isEmpty() &&
                sshift.getText().isEmpty() && ssalary.getText().isEmpty() && ssection.getText().isEmpty() &&
                shire_date.getValue() == null && spassword.getText().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your search criterion");
            alert.showAndWait();
        }
        else if(!id_button.isSelected() && !national_id_button.isSelected() && !hire_date_button.isSelected() && !name_button.isSelected() &&
                !salary_button.isSelected() && !shift_button.isSelected() && !section_button.isSelected() &&
                !phone_button.isSelected() && !password_button.isSelected()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select at least 1 column to show from the check boxes");
            alert.showAndWait();
        }
        else {
            query = "select ";
            if (id_button.isSelected()) query += "id, ";
            if (national_id_button.isSelected()) query += "national_id, ";
            if (name_button.isSelected()) query += "emp_name, ";
            if (phone_button.isSelected()) query += "phone, ";
            if (section_button.isSelected()) query += "section_id, ";
            if (shift_button.isSelected()) query += "shift, ";
            if (salary_button.isSelected()) query += "salary, ";
            if (hire_date_button.isSelected()) query += "hire_date, ";
            if (password_button.isSelected()) query += "password, ";

            query = query.substring(0, query.length() - 2) + " from employee where ";

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

            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<String> arrayList = new ArrayList<>();

            table.getItems().clear();
            table.getColumns().clear();

            if (id_button.isSelected()) {
                addColumn("ID", "id");
                arrayList.add("id");
            }
            if (national_id_button.isSelected()) {
                addColumn("National ID", "national_id");
                arrayList.add("national_id");
            }
            if (name_button.isSelected()) {
                addColumn("Name", "name");
                arrayList.add("name");
            }
            if (phone_button.isSelected()) {
                addColumn("Phone", "phone");
                arrayList.add("phone");
            }
            if (section_button.isSelected()) {
                addColumn("Section", "section_id");
                arrayList.add("section_id");
            }
            if (shift_button.isSelected()) {
                addColumn("Shift", "shift");
                arrayList.add("shift");
            }
            if (salary_button.isSelected()) {
                TableColumn<Employee, Double> column = new TableColumn<>();
                column.setText("Salary");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Employee, Double>("salary"));
                table.getColumns().add(column);
                arrayList.add("salary");
            }
            if (hire_date_button.isSelected()) {
                addColumn("Hire Date", "hire_date");
                arrayList.add("hire_date");
            }
            if (password_button.isSelected()) {
                addColumn("Password", "password");
                arrayList.add("password");
            }

            while (resultSet.next()) {
                Employee emp = new Employee();
                for (int i = 0; i < arrayList.size(); i++) {
                    switch (arrayList.get(i)) {
                        case "password":
                            emp.setPassword(resultSet.getString(i + 1));
                            break;
                        case "id":
                            emp.setId(resultSet.getString(i + 1));
                            break;
                        case "national_id":
                            emp.setNational_id(resultSet.getString(i + 1));
                            break;
                        case "name":
                            emp.setName(resultSet.getString(i + 1));
                            break;
                        case "phone":
                            emp.setPhone(resultSet.getString(i + 1));
                            break;
                        case "section_id":
                            emp.setSection_id(resultSet.getString(i + 1));
                            break;
                        case "salary":
                            emp.setSalary(resultSet.getDouble(i + 1));
                            break;
                        case "hire_date":
                            emp.setHire_date(resultSet.getString(i + 1));
                            break;
                        case "shift":
                            emp.setShift(resultSet.getString(i + 1));
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
                list.add(emp);
            }
            table.setItems(list);
            con.close();
        }
    }

    @FXML
    private void addColumn(String Name, String name) {
        TableColumn<Employee, String> column = new TableColumn<>();
        column.setText(Name);
        column.setMinWidth(130);
        column.setCellValueFactory(new PropertyValueFactory<Employee, String>(name));
        table.getColumns().add(column);
    }

    @FXML
    void backClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Employees");
        stage. setResizable(false);
        stage.show();
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
