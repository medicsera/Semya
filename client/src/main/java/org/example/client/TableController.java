package org.example.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TableController {
    @FXML
    private TableView<Reservation> tableView;
    @FXML
    private TableColumn<Reservation, String> nameColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> timeColumn;
    @FXML
    private TableColumn<Reservation, String> phoneColumn;

    private final ObservableList<Reservation> data = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        tableView.setItems(data);
    }

    @FXML
    private TextField voidname;
    @FXML
    private TextField voiddate;
    @FXML
    private TextField voidtime;
    @FXML
    private TextField voidphone;

    @FXML
    private void handleAddReservation() {
        String name = voidname.getText();
        String date = voiddate.getText();
        String time = voidtime.getText();
        String phone = voidphone.getText();

        if (!name.isEmpty() && !date.isEmpty() && !time.isEmpty()&& !phone.isEmpty()) {
            data.add(new Reservation(name, date, time, phone));
            voidname.clear();
            voiddate.clear();
            voidtime.clear();
            voidphone.clear();
        }

    }
}
