package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Main implements Initializable {

    static HashMap<String, Integer> id = new HashMap<>();
    static HashMap<Integer, String> id2 = new HashMap<>();
    final static int N = 20, infinity = 100000000;
    static ArrayList<ArrayList<ArrayList<Integer>>> adj = new ArrayList<>();

    @FXML
    private RadioButton acre;

    @FXML
    private RadioButton as_button;

    @FXML
    private RadioButton bfs_button;

    @FXML
    private RadioButton uc_button;

    @FXML
    private RadioButton betlehem;

    @FXML
    private RadioButton gaza;

    @FXML
    private Button goal;

    @FXML
    private RadioButton haifa;

    @FXML
    private RadioButton hebron;

    @FXML
    private ImageView img;

    @FXML
    private RadioButton jafa;

    @FXML
    private RadioButton jenin;

    @FXML
    private RadioButton jericho;

    @FXML
    private RadioButton jerusalem;

    @FXML
    private RadioButton nablus;

    @FXML
    private RadioButton nazareth;

    @FXML
    private ToggleGroup tg1;

    @FXML
    private ToggleGroup tg2;

    @FXML
    private RadioButton qalqilya;

    @FXML
    private RadioButton rafah;

    @FXML
    private RadioButton ramallah;

    @FXML
    private RadioButton ramla;

    @FXML
    private Button run;

    @FXML
    private RadioButton safed;

    @FXML
    private RadioButton salfit;

    @FXML
    private RadioButton sheva;

    @FXML
    private Button start;

    @FXML
    private RadioButton tiberias;

    @FXML
    private RadioButton tulkarm;

    static String startNode = null, endNode = null;

    static int[] cost, parent, h2;

    @FXML
    void runAlg(MouseEvent event) throws IOException {
        if (startNode == null || endNode == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Make sure to select a start node and an end node");
            alert.showAndWait();
        }
        else {
            Stage stage;
            Scene scene;
            Parent root;
            if (bfs_button.isSelected()) {
                Algorithms.startNode = startNode;
                Algorithms.endNode = endNode;
                Pair<int[], int[]> p = Algorithms.breadthFirstSearch(adj, id);
                cost = p.getFirst();
                parent = p.getSecond();
                root = FXMLLoader.load(getClass().getResource("bfs.fxml"));
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("BFS Run");
                stage.show();
            }
            else if (as_button.isSelected()) {
                Algorithms.startNode = startNode;
                Algorithms.endNode = endNode;
                Triple<int[], int[], int[]> t = Algorithms.AStarH1(adj, id);
                cost = t.getFirst();
                parent = t.getSecond();
                h2 = t.getThird();
                root = FXMLLoader.load(getClass().getResource("ASTAR.fxml"));
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("A* Run");
                stage.show();
            }
            else if (uc_button.isSelected()) {
                Algorithms.startNode = startNode;
                Algorithms.endNode = endNode;
                Pair<int[], int[]> p = Algorithms.UniformCost(adj, id);
                cost = p.getFirst();
                parent = p.getSecond();
                root = FXMLLoader.load(getClass().getResource("UC.fxml"));
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Uniform Cost Run");
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Make sure to select an algorithm");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void selectGoal(MouseEvent event) {
        if(acre.isSelected()){
            endNode = acre.getText().toLowerCase();
        }
        else if(betlehem.isSelected()){
            endNode = betlehem.getText().toLowerCase();
        }
        else if(gaza.isSelected()){
            endNode = gaza.getText().toLowerCase();
        }
        else if(haifa.isSelected()){
            endNode = haifa.getText().toLowerCase();
        }
        else if(hebron.isSelected()){
            endNode = hebron.getText().toLowerCase();
        }
        else if(jafa.isSelected()){
            endNode = jafa.getText().toLowerCase();
        }
        else if(jenin.isSelected()){
            endNode = jenin.getText().toLowerCase();
        }
        else if(jericho.isSelected()){
            endNode = jericho.getText().toLowerCase();
        }
        else if(jerusalem.isSelected()){
            endNode = jerusalem.getText().toLowerCase();
        }
        else if(nablus.isSelected()){
            endNode = nablus.getText().toLowerCase();
        }
        else if(nazareth.isSelected()){
            endNode = nazareth.getText().toLowerCase();
        }
        else if(qalqilya.isSelected()){
            endNode = qalqilya.getText().toLowerCase();
        }
        else if(rafah.isSelected()){
            endNode = rafah.getText().toLowerCase();
        }
        else if(ramallah.isSelected()){
            endNode = ramallah.getText().toLowerCase();
        }
        else if(ramla.isSelected()){
            endNode = ramla.getText().toLowerCase();
        }
        else if(safed.isSelected()){
            endNode = safed.getText().toLowerCase();
        }
        else if(salfit.isSelected()){
            endNode = salfit.getText().toLowerCase();
        }
        else if(sheva.isSelected()){
            endNode = sheva.getText().toLowerCase();
        }
        else if(tiberias.isSelected()){
            endNode = tiberias.getText().toLowerCase();
        }
        else if(tulkarm.isSelected()){
            endNode = tulkarm.getText().toLowerCase();
        }
    }

    @FXML
    void selectStart(MouseEvent event) {
        if(acre.isSelected()){
            startNode = acre.getText().toLowerCase();
        }
        else if(betlehem.isSelected()){
            startNode = betlehem.getText().toLowerCase();
        }
        else if(gaza.isSelected()){
            startNode = gaza.getText().toLowerCase();
        }
        else if(haifa.isSelected()){
            startNode = haifa.getText().toLowerCase();
        }
        else if(hebron.isSelected()){
            startNode = hebron.getText().toLowerCase();
        }
        else if(jafa.isSelected()){
            startNode = jafa.getText().toLowerCase();
        }
        else if(jenin.isSelected()){
            startNode = jenin.getText().toLowerCase();
        }
        else if(jericho.isSelected()){
            startNode = jericho.getText().toLowerCase();
        }
        else if(jerusalem.isSelected()){
            startNode = jerusalem.getText().toLowerCase();
        }
        else if(nablus.isSelected()){
            startNode = nablus.getText().toLowerCase();
        }
        else if(nazareth.isSelected()){
            startNode = nazareth.getText().toLowerCase();
        }
        else if(qalqilya.isSelected()){
            startNode = qalqilya.getText().toLowerCase();
        }
        else if(rafah.isSelected()){
            startNode = rafah.getText().toLowerCase();
        }
        else if(ramallah.isSelected()){
            startNode = ramallah.getText().toLowerCase();
        }
        else if(ramla.isSelected()){
            startNode = ramla.getText().toLowerCase();
        }
        else if(safed.isSelected()){
            startNode = safed.getText().toLowerCase();
        }
        else if(salfit.isSelected()){
            startNode = salfit.getText().toLowerCase();
        }
        else if(sheva.isSelected()){
            startNode = sheva.getText().toLowerCase();
        }
        else if(tiberias.isSelected()){
            startNode = tiberias.getText().toLowerCase();
        }
        else if(tulkarm.isSelected()){
            startNode = tulkarm.getText().toLowerCase();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acre.setToggleGroup(tg1);
        betlehem.setToggleGroup(tg1);
        gaza.setToggleGroup(tg1);
        haifa.setToggleGroup(tg1);
        hebron.setToggleGroup(tg1);
        jafa.setToggleGroup(tg1);
        jenin.setToggleGroup(tg1);
        jericho.setToggleGroup(tg1);
        jerusalem.setToggleGroup(tg1);
        nablus.setToggleGroup(tg1);
        nazareth.setToggleGroup(tg1);
        qalqilya.setToggleGroup(tg1);
        rafah.setToggleGroup(tg1);
        ramallah.setToggleGroup(tg1);
        ramla.setToggleGroup(tg1);
        safed.setToggleGroup(tg1);
        salfit.setToggleGroup(tg1);
        sheva.setToggleGroup(tg1);
        tiberias.setToggleGroup(tg1);
        tulkarm.setToggleGroup(tg1);
        bfs_button.setToggleGroup(tg2);
        uc_button.setToggleGroup(tg2);
        as_button.setToggleGroup(tg2);
        startNode = endNode = null;
        adj.clear();
        File file = new File("src/sample/nodes.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0; i <= N; i++){
            adj.add(new ArrayList<>());
        }
        int cnt = 1;
        while (true) {
            assert scanner != null;
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();
            String [] tokens = line.split(" ");
            tokens[0] = tokens[0].toLowerCase();
            tokens[1] = tokens[1].toLowerCase();
            if(!id.containsKey(tokens[0])){
                id.put(tokens[0], cnt++);
                id2.put(cnt - 1, tokens[0]);
            }
            if(!id.containsKey(tokens[1])){
                id.put(tokens[1], cnt++);
                id2.put(cnt - 1, tokens[1]);
            }
            ArrayList<Integer> values = new ArrayList<>();
            values.add(id.get(tokens[1]));
            values.add(Integer.parseInt(tokens[2]));
            values.add(Integer.parseInt(tokens[3]));
            values.add(Integer.parseInt(tokens[4]));
            adj.get(id.get(tokens[0])).add(values);

            ArrayList<Integer> values2 = new ArrayList<>();

            values2.add(id.get(tokens[0]));
            values2.add(Integer.parseInt(tokens[2]));
            values2.add(Integer.parseInt(tokens[3]));
            values2.add(Integer.parseInt(tokens[4]));
            adj.get(id.get(tokens[1])).add(values2);
        }
        scanner.close();
    }
}
