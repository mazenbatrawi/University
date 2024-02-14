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

public class SearchEmpInSections {

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
    private ImageView search_image;

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
    private CheckBox section_id_button;

    @FXML
    private CheckBox section_name_button;

    @FXML
    private CheckBox shift_button;

    @FXML
    private TextField sid;

    @FXML
    private TextField sname;

    @FXML
    private TableView<EmployeeSection> table;

    ObservableList<EmployeeSection> list = FXCollections.observableArrayList();

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

        else if(!id_button.isSelected() && !national_id_button.isSelected() && !hire_date_button.isSelected() && !name_button.isSelected() &&
                !salary_button.isSelected() && !shift_button.isSelected() && !section_id_button.isSelected() && !phone_button.isSelected()
                && !section_name_button.isSelected() && !password_button.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select at least 1 column to show from the check boxes");
            alert.showAndWait();
        }
        else{
            query = "select ";

            if(section_id_button.isSelected()) query += "Section.section_id, ";
            if(section_name_button.isSelected()) query += "Section.section_name, ";
            if(id_button.isSelected()) query += "employee.id, ";
            if(national_id_button.isSelected()) query += "employee.national_id, ";
            if(name_button.isSelected()) query += "employee.emp_name, ";
            if(phone_button.isSelected()) query += "employee.phone, ";
            if(shift_button.isSelected()) query += "employee.shift, ";
            if(salary_button.isSelected()) query += "employee.salary, ";
            if(hire_date_button.isSelected()) query += "employee.hire_date, ";
            if(password_button.isSelected()) query += "employee.password, ";

            query = query.substring(0, query.length() - 2) + " from employee, Section where ";

            if(!sid.getText().isEmpty()) query += "section.section_id = " + sid.getText() + " and ";
            if(!sname.getText().isEmpty()) query += "section.section_name = \"" + sname.getText() + "\" and ";

            query = query.substring(0, query.length() - 5);
            query += " and section.section_id = employee.section_id";

            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<String> arrayList = new ArrayList<>();

            table.getItems().clear();
            table.getColumns().clear();

            if(section_id_button.isSelected()) {addColumn("Section ID", "section_id"); arrayList.add("section_id");}
            if(section_name_button.isSelected()) {addColumn("Section Name", "section_name"); arrayList.add("section_name");}
            if(id_button.isSelected()) {addColumn("Employee ID", "id"); arrayList.add("id");}
            if(national_id_button.isSelected()) {addColumn("National ID", "national_id"); arrayList.add("national_id");}
            if(name_button.isSelected()) {addColumn("Employee Name", "name"); arrayList.add("name");}
            if(phone_button.isSelected()) {addColumn("Phone", "phone"); arrayList.add("phone");}
            if(shift_button.isSelected()) {addColumn("Shift", "shift"); arrayList.add("shift");}
            if(salary_button.isSelected()){
                TableColumn<EmployeeSection, Double> column = new TableColumn<>();
                column.setText("Salary");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<EmployeeSection, Double>("salary"));
                table.getColumns().add(column);
                arrayList.add("salary");
            }
            if(hire_date_button.isSelected()) {addColumn("Hire Date", "hire_date"); arrayList.add("hire_date");}
            if(password_button.isSelected()) {addColumn("Password", "password"); arrayList.add("password");}

            while(resultSet.next()){
                EmployeeSection employeeSection = new EmployeeSection();
                for(int i = 0; i < arrayList.size(); i++){
                    switch (arrayList.get(i)) {
                        case "section_id":
                            employeeSection.setSection_id(resultSet.getString(i + 1));
                            break;
                        case "password":
                            employeeSection.setPassword(resultSet.getString(i + 1));
                            break;
                        case "section_name":
                            employeeSection.setSection_name(resultSet.getString(i + 1));
                            break;
                        case "id":
                            employeeSection.setId(resultSet.getString(i + 1));
                            break;
                        case "national_id":
                            employeeSection.setNational_id(resultSet.getString(i + 1));
                            break;
                        case "name":
                            employeeSection.setName(resultSet.getString(i + 1));
                            break;
                        case "phone":
                            employeeSection.setPhone(resultSet.getString(i + 1));
                            break;
                        case "salary":
                            employeeSection.setSalary(resultSet.getDouble(i + 1));
                            break;
                        case "hire_date":
                            employeeSection.setHire_date(resultSet.getString(i + 1));
                            break;
                        case "shift":
                            employeeSection.setShift(resultSet.getString(i + 1));
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
                list.add(employeeSection);
            }
            table.setItems(list);
            con.close();
        }
    }

    @FXML
    private void addColumn(String Name, String name) {
        TableColumn<EmployeeSection, String> column = new TableColumn<>();
        column.setText(Name);
        column.setMinWidth(130);
        column.setCellValueFactory(new PropertyValueFactory<EmployeeSection, String>(name));
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
