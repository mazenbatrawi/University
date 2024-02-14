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

public class SearchProduct {

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
    private CheckBox barcode_button;

    @FXML
    private CheckBox company_id_button;

    @FXML
    private CheckBox expire_date_button;

    @FXML
    private CheckBox name_button;

    @FXML
    private CheckBox price_button;

    @FXML
    private CheckBox production_date_button;

    @FXML
    private CheckBox quantity_button;

    @FXML
    private TextField sbarcode;

    @FXML
    private TextField scompany_id;

    @FXML
    private CheckBox section_id_button;

    @FXML
    private DatePicker sexpire_date;

    @FXML
    private TextField sname;

    @FXML
    private DatePicker sproduction_date;

    @FXML
    private TextField squantity;

    @FXML
    private TextField ssection_id;

    @FXML
    private TextField sprice;

    @FXML
    private TableView<Product> table;

    @FXML
    private ImageView back_image;

    @FXML
    private ImageView search_image;

    @FXML
    private CheckBox selling_price_button;

    @FXML
    private TextField sselling_price;

    ObservableList<Product> list = FXCollections.observableArrayList();

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
    void search(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!barcode_button.isSelected() && !name_button.isSelected() && !company_id_button.isSelected() && !section_id_button.isSelected()
            && !price_button.isSelected() && !quantity_button.isSelected() && !production_date_button.isSelected() &&
                !expire_date_button.isSelected() && !selling_price_button.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Select at least 1 column to show from the check boxes");
            alert.showAndWait();
        }
        else if(sbarcode.getText().isEmpty() && sname.getText().isEmpty() && scompany_id.getText().isEmpty() && ssection_id.getText().isEmpty() &&
                sprice.getText().isEmpty() && squantity.getText().isEmpty() && sproduction_date.getValue() == null &&
                sexpire_date.getValue() == null && sselling_price.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill at least one field for your search criterion");
            alert.showAndWait();
        }
        else{
            query = "select ";
            if (barcode_button.isSelected()) query += "barcode, ";
            if (name_button.isSelected()) query += "product_name, ";
            if (company_id_button.isSelected()) query += "company_id, ";
            if (section_id_button.isSelected()) query += "section_id, ";
            if (price_button.isSelected()) query += "price, ";
            if (quantity_button.isSelected()) query += "total_quantity, ";
            if (production_date_button.isSelected()) query += "production_date, ";
            if (expire_date_button.isSelected()) query += "expire_date, ";
            if (selling_price_button.isSelected()) query += "selling_price, ";

            query = query.substring(0, query.length() - 2) + " from product where ";

            if (!sbarcode.getText().isEmpty()) query += "barcode = \"" + sbarcode.getText() + "\" and ";
            if (!sname.getText().isEmpty()) query += "product_name = \"" + sname.getText() + "\" and ";
            if (!scompany_id.getText().isEmpty()) query += "company_id = " + scompany_id.getText() + " and ";
            if (!ssection_id.getText().isEmpty()) query += "section_id = " + ssection_id.getText() + " and ";
            if (!sprice.getText().isEmpty()) query += "price = " + sprice.getText() + " and ";
            if (!squantity.getText().isEmpty()) query += "total_quantity = " + squantity.getText() + " and ";
            if (sproduction_date.getValue() != null) query += "production_date = \"" + sproduction_date.getValue().toString() + "\" and ";
            if (sexpire_date.getValue() != null) query += "expire_date = \"" + sexpire_date.getValue().toString() + "\" and ";
            if (!sselling_price.getText().isEmpty()) query += "selling_price = " + sselling_price.getText() + " and ";

            query = query.substring(0, query.length() - 5);

            connectDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<String> arrayList = new ArrayList<>();

            table.getItems().clear();
            table.getColumns().clear();

            if (barcode_button.isSelected()) {
                addColumn("Barcode", "barcode");
                arrayList.add("barcode");
            }
            if (name_button.isSelected()) {
                addColumn("Name", "name");
                arrayList.add("name");
            }
            if (company_id_button.isSelected()) {
                TableColumn<Product, Integer> column = new TableColumn<>();
                column.setText("Company ID");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, Integer>("company_id"));
                table.getColumns().add(column);
                arrayList.add("company_id");
            }
            if (section_id_button.isSelected()) {
                TableColumn<Product, Integer> column = new TableColumn<>();
                column.setText("Section ID");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, Integer>("section_id"));
                table.getColumns().add(column);
                arrayList.add("section_id");
            }
            if (price_button.isSelected()) {
                TableColumn<Product, Double> column = new TableColumn<>();
                column.setText("Price");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
                table.getColumns().add(column);
                arrayList.add("price");
            }
            if (quantity_button.isSelected()) {
                TableColumn<Product, Integer> column = new TableColumn<>();
                column.setText("Quantity");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, Integer>("total_quantity"));
                table.getColumns().add(column);
                arrayList.add("total_quantity");
            }
            if (production_date_button.isSelected()) {
                addColumn("Production Date", "production_date");
                arrayList.add("production_date");
            }
            if (expire_date_button.isSelected()) {
                addColumn("Expire Date", "expire_date");
                arrayList.add("expire_date");
            }

            if (selling_price_button.isSelected()) {
                TableColumn<Product, Double> column = new TableColumn<>();
                column.setText("Selling Price");
                column.setMinWidth(130);
                column.setCellValueFactory(new PropertyValueFactory<Product, Double>("selling_price"));
                table.getColumns().add(column);
                arrayList.add("selling_price");
            }

            while (resultSet.next()) {
                Product product = new Product();
                for (int i = 0; i < arrayList.size(); i++) {
                    switch (arrayList.get(i)) {
                        case "barcode":
                            product.setBarcode(resultSet.getString(i + 1));
                            break;
                        case "selling_price":
                            product.setSelling_price(Double.parseDouble(resultSet.getString(i + 1)));
                            break;
                        case "name":
                            product.setName(resultSet.getString(i + 1));
                            break;
                        case "company_id":
                            product.setCompany_id(resultSet.getString(i + 1));
                            break;
                        case "section_id":
                            product.setSection_id(resultSet.getString(i + 1));
                            break;
                        case "price":
                            product.setPrice(Double.parseDouble(resultSet.getString(i + 1)));
                            break;
                        case "total_quantity":
                            product.setTotal_quantity(Integer.parseInt(resultSet.getString(i + 1)));
                            break;
                        case "production_date":
                            product.setProduction_date(resultSet.getString(i + 1));
                            break;
                        case "expire_date":
                            product.setExpire_date(resultSet.getString(i + 1));
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
                list.add(product);
            }

            table.setItems(list);
            con.close();
        }
    }

    @FXML
    private void addColumn(String Name, String name) {
        TableColumn<Product, String> column = new TableColumn<>();
        column.setText(Name);
        column.setMinWidth(130);
        column.setCellValueFactory(new PropertyValueFactory<Product, String>(name));
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
