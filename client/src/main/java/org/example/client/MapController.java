package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    public void SwitchToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.setTakedTable(tableNum);
        stage = (Stage) selectButton.getScene().getWindow();
        scene = new Scene(root, 415, 657);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Label table1,table2,table3,table4,table5,table6,table7,table8;
    @FXML
    private Button selectButton;

    @FXML
    private RadioButton btable1,btable2,btable3,btable4,btable5,btable6,btable7,btable8;

    private final List<Integer> numbers = new ArrayList<>();
    int tableNum = 0;

    public void gettable(ActionEvent event) {
        if (btable1.isSelected()) {
            table1.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 1;
        } else {
            table1.getStyleClass().remove("busy");
        }
        if (btable2.isSelected()) {
            table2.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 2;
        } else {
            table2.getStyleClass().remove("busy");
        }
        if (btable3.isSelected()) {
            table3.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 3;
        } else {
            table3.getStyleClass().remove("busy");
        }
        if (btable4.isSelected()) {
            table4.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 4;
        } else {
            table4.getStyleClass().remove("busy");
        }
        if (btable5.isSelected()) {
            table5.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 5;
        } else {
            table5.getStyleClass().remove("busy");
        }
        if (btable6.isSelected()) {
            table6.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 6;
        } else {
            table6.getStyleClass().remove("busy");
        }
        if (btable7.isSelected()) {
            table7.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 7;
        } else {
            table7.getStyleClass().remove("busy");
        }
        if (btable8.isSelected()) {
            table8.getStyleClass().add("busy");
            selectButton.setDisable(false);
            tableNum = 8;
        } else{
            table8.getStyleClass().remove("busy");
        }
    }
    public void BselectButton() {
        numbers.add(tableNum);
        try{
            SwitchToMenu();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        }






}
