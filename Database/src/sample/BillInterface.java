package sample;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.stage.*;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class BillInterface implements Initializable {

    Bill temp;
    Date lastClickTime;

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

    @FXML
    private TableColumn<Bill, String> barcode;

    @FXML
    private TableColumn<Bill, Double> cost;

    @FXML
    private TableColumn<Bill, String> date;

    @FXML
    private TableColumn<Bill, String> emp_id;

    @FXML
    private TableView<Bill> table;

    @FXML
    private ImageView search_image;

    ObservableList<Bill> list = FXCollections.observableArrayList();

    static String bill_id2, emp_id2, date2;

    public static void main(String[] args){
    }

    @FXML
    void search(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SearchBillController.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search Bills");
        stage. setResizable(false);
        stage.show();
    }

    @FXML
    void handleRowSelect(MouseEvent event) throws IOException {
        Bill bill = table.getSelectionModel().getSelectedItem();
        if (bill == null) return;
        if(bill != temp){
            temp = bill;
            lastClickTime = new Date();
        }
        else {
            Date now = new Date();
            long diff = now.getTime() - lastClickTime.getTime();
            if (diff < 300){
                bill_id2 = bill.getId();
                date2 = bill.getDate();
                emp_id2 = bill.getEmp_id();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
                Parent root2 = loader.load();
                Scene scene2 = new Scene(root2);
                Stage stage2 = new Stage();
                stage2.setScene(scene2);
                stage2.setTitle("Receipt Details");
                stage2. setResizable(false);
                stage2.show();
            }
            else {
                lastClickTime = new Date();
            }
        }
    }

    @FXML
    public void readData(){
        try {
            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from bill order by id asc");
            while(resultSet.next()){
                list.add(new Bill(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        Double.parseDouble(resultSet.getString(4))
                ));
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle rb) {
        readData();
        barcode.setCellValueFactory(new PropertyValueFactory<Bill, String>("id"));
        cost.setCellValueFactory(new PropertyValueFactory<Bill, Double>("cost"));
        date.setCellValueFactory(new PropertyValueFactory<Bill, String>("date"));
        emp_id.setCellValueFactory(new PropertyValueFactory<Bill, String>("emp_id"));
        table.setItems(list);

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
