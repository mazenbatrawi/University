package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class ASTAR implements Initializable{

    @FXML
    private Label end_city;

    @FXML
    private Label from_to;

    @FXML
    private Button info;

    @FXML
    private Label path;

    @FXML
    private Label start_city;

    @FXML
    private Label temp;

    @FXML
    private Label total_cost;

    static int path_cost = 0;
    static int [] cost, parent;

    @FXML
    void click(MouseEvent event) throws IOException {
        Algorithms.startNode = Main.startNode;
        Algorithms.endNode = Main.endNode;
        Pair<int[], int[]> p = Algorithms.AStarH2(Main.adj, Main.id, Main.h2);
        cost = p.getFirst();
        parent = p.getSecond();
        path_cost = cost[Main.id.get(Main.endNode)];
        Parent root = FXMLLoader.load(getClass().getResource("ASTAR2.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Second A* Run");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cost = Main.cost[Main.id.get(Main.endNode)];
        if(cost == Main.infinity){
            from_to.setText("There is no path between " + Main.startNode + " and " + Main.endNode + ".");
        }
        else {
            int node = Main.id.get(Main.endNode);
            Stack<String> stack = new Stack<>();
            while (true) {
                stack.add(Main.id2.get(node));
                if (Main.parent[node] == -1) {
                    break;
                }
                node = Main.parent[node];
            }
            StringBuilder path2 = new StringBuilder();
            while (!stack.isEmpty()) {
                String s = stack.peek();
                String firstLetStr = s.substring(0, 1);
                String remLetStr = s.substring(1);
                firstLetStr = firstLetStr.toUpperCase();
                s = firstLetStr + remLetStr + " ";
                path2.append(s);
                stack.pop();
                if (!stack.isEmpty()) {
                    path2.append("-> ");
                }
            }
            String start = (Main.startNode.substring(0, 1)).toUpperCase() + Main.startNode.substring(1);
            String end = (Main.endNode.substring(0, 1)).toUpperCase() + Main.endNode.substring(1);
            temp.setText("- We are travelling from " + start + " to " + end);
            from_to.setText("- The path we went through is: ");
            start_city.setText("- The start city: " + start);
            end_city.setText("- The goal city: " + end);
            total_cost.setText("- The total distance is: " + cost + " KM.");
            path.setStyle("-fx-font-weight: bold;");
            temp.setStyle("-fx-font-weight: bold;");
            path.setTextFill(Color.color(1, 0, 0));
            from_to.setStyle("-fx-font-weight: bold;");
            start_city.setStyle("-fx-font-weight: bold;");
            end_city.setStyle("-fx-font-weight: bold;");
            total_cost.setStyle("-fx-font-weight: bold;");
            info.setStyle("-fx-font-size:20;-fx-font-weight: bold;");
            path.setText(path2.toString());
        }
    }
}
