package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

class D {
    String date;
    D(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date;
    }
}
class sortComp implements Comparator<D> {
    @Override
    public int compare(D a, D b){
        String date1 = a.getDate();
        String date2 = b.getDate();
        int day1 = (date1.charAt(8) - '0') * 10 + date1.charAt(9) - '0';
        int month1 = (date1.charAt(5) - '0') * 10 + date1.charAt(6) - '0';
        int year1 = (date1.charAt(0) - '0') * 1000 + (date1.charAt(1) - '0') * 100 + (date1.charAt(2) - '0') * 10 +
                (date1.charAt(3) - '0');

        int day2 = (date2.charAt(8) - '0') * 10 + date2.charAt(9) - '0';
        int month2 = (date2.charAt(5) - '0') * 10 + date2.charAt(6) - '0';
        int year2 = (date2.charAt(0) - '0') * 1000 + (date2.charAt(1) - '0') * 100 + (date2.charAt(2) - '0') * 10 +
                (date2.charAt(3) - '0');

        if(year1 == year2){
            if(month1 == month2){
                if(day1 == day2){
                    return 0;
                }
                return (day1 > day2 ? 1 : -1);
            }
            return (month1 > month2 ? 1 : -1);
        }
        return (year1 > year2 ? 1 : -1);

    }
}

public class Statistics implements Initializable {

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
    private BarChart<String, Double> bchart;

    @FXML
    private TableColumn<Employee, String> name;

    @FXML
    private TableColumn<Employee, Double> salary;

    @FXML
    private TableView<Employee> table;

    @FXML
    private AreaChart<String, Double> chart;

    @FXML
    private Text total_income;

    @FXML
    private Text profit_text;

    @FXML
    private Text prof_percentage;

    @FXML
    private ImageView home_image;

    ObservableList<Employee> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chart.getData().clear();
        name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        salary.setCellValueFactory(new PropertyValueFactory<Employee, Double>("salary"));

        try {
            connectDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        ResultSet resultSet = null;
        query = "select emp_name, salary from employee";
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert statement != null;
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true){
            try {
                assert resultSet != null;
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Employee employee = new Employee();
            try {
                employee.setName(resultSet.getString(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                employee.setSalary(resultSet.getDouble(2));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            list.add(employee);
        }
        table.setItems(list);
        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
        query = "select datee, cost from bill";
        statement = null;
        resultSet = null;
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert statement != null;
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ArrayList<D> arrayList = new ArrayList<>();
        HashMap<String, Double> hashMap = new HashMap<>();
        double total = 0;

        while(true){
            try {
                assert resultSet != null;
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String date = null;
            try {
                date = resultSet.getString(1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                total += Double.parseDouble(resultSet.getString(2));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert date != null;
            double cost = 0;
            try {
                cost = Double.parseDouble(resultSet.getString(2));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(hashMap.containsKey(date)){
                double temp = hashMap.get(date);
                temp += cost;
                hashMap.put(date, temp);
            }
            else{
                arrayList.add(new D(date));
                hashMap.put(date, cost);
            }
        }

        arrayList.sort(new sortComp());

        for(D g : arrayList){
            series.getData().add(new XYChart.Data<String, Double>(g.toString(), hashMap.get(g.toString())));
        }

        series.setName("Total Income In The Date Shown");
        chart.getData().add(series);
        total_income.setText("Total income so far: " + total + "$.");

        query = "select * from bill_product";
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        double profit = 0;
        while(true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                query = "select price from product where barcode = " + resultSet.getString(2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ResultSet resultSet1 = null;
            try {
                statement = con.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                resultSet1 = statement.executeQuery(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            double price = 0;
            while(true){
                try {
                    assert resultSet1 != null;
                    if (!resultSet1.next()) break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    price = resultSet1.getDouble(1);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                profit += (-price + resultSet.getDouble(4)) * resultSet.getInt(3);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        profit_text.setText("The total profit is: " + profit + "$.");
        double profit_percentage = 100 * profit / total;
        DecimalFormat df = new DecimalFormat(".00");
        String t = df.format(profit_percentage);
        prof_percentage.setText("The Profit Ratio is: " + t  + "%.");

        query = "select employee_id, cost from bill";
        try {
            statement = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        HashMap<String, Double> total_employee = new HashMap<>();
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if(total_employee.containsKey(resultSet.getString(1))){
                    double temp = resultSet.getDouble(2);
                    temp += total_employee.get(resultSet.getString(1));
                    total_employee.put(resultSet.getString(1), temp);
                }
                else{
                    total_employee.put(resultSet.getString(1), resultSet.getDouble(2));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        for (HashMap.Entry<String, Double> entry : total_employee.entrySet()) {
            query = "select emp_name from employee where id = " + entry.getKey();
            try {
                statement = con.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                resultSet = statement.executeQuery(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while(true) {
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    series1.getData().add(new XYChart.Data<String, Double>(resultSet.getString(1), entry.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        series1.setName("Total Sold");
        bchart.getData().add(series1);
        try {
            con.close();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
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

    @FXML
    void home(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home Page");
        stage. setResizable(false);
        stage.show();
    }
}
