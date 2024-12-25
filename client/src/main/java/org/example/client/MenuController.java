package org.example.client;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.shared.orderData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.*;

public class MenuController  implements Initializable {

    @FXML private TableView<orderData> orderTableView;
    @FXML private TableColumn<orderData, Integer> orderColtableid;
    @FXML private TableColumn<orderData, String> orderColname;
    @FXML private TableColumn<orderData, String> orderColphone;
    @FXML private TableColumn<orderData, String> orderColdate;
    @FXML private TableColumn<orderData, String> orderColtime;
    @FXML private TableColumn<orderData, String> orderColwishes;

    private final ObservableList<orderData> order = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(MenuController.class.getName());

    String cssfree = this.getClass().getResource("StyleButtonTableFree.css").toExternalForm();

    String cssbusy = this.getClass().getResource("StyleButtonTableBusy.css").toExternalForm();

    @FXML
    private TextField takedTable;
    @FXML
    private AnchorPane pane1;

    private Stage stage;
    private Scene scene;

    public void SwitchToMap(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("map.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(cssfree);
        scene.getStylesheets().add(cssbusy);
        stage.setScene(scene);
        stage.show();
    }

    public void setTakedTable(int tableNum) {
        takedTable.replaceSelection(String.valueOf(tableNum));
    }
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    @FXML
    private TextField voidname;
    @FXML
    private TextField voidphone;
    @FXML
    private TextField voiddate;
    @FXML
    private TextField voidtime;
    @FXML
    private TextArea voidwishes;
    @FXML
    private Button tablebutton;

    private void showAlert(String title, String message) {
        logger.info("Вызов Alert");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setupLogger() {
        try {
            //удаление стандартных обработчиков
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            //создание обработчика для консоли
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());

            //создание обработчика для файла
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());

            //добавление обработчиков к логгеру
            logger.addHandler(consoleHandler);
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось настроить логирование.");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLogger();
        logger.info("Инициализация контроллера");
        //подключение
        try {
            socket = new Socket("127.0.0.1", 2048);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Connection Error", "Не удалось подключиться к серверу.");
        }

        orderShowListData();
    }

    @FXML
    private void onAdd() {
        logger.info("Попытка добавления новой записи");
        String table = takedTable.getText();
        String name = voidname.getText();
        String phone = voidphone.getText();
        String date = voiddate.getText();
        String time = voidtime.getText();
        String wishes = voidwishes.getText();


        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() ||
                time.isEmpty() || wishes.isEmpty() || table.isEmpty()) {
            showAlert("Внимание", "Пожалуйста, заполните все поля.");
            return;
        }

        String command = String.join("|", "ADD", table, name, phone, date, time, wishes);
        out.println(command);

        try {
            String response = in.readLine();
            if (response == null) {
                showAlert("Ошибка", "Ответ сервера не получен.");
                return;
            }
            if (response.startsWith("SUCCESS|")) {
                showAlert("Успех", response.substring(5));
                loadData();
                onClear();
                logger.info("Новая запись успешно добавлена: " + time + " " + wishes);
            } else if (response.startsWith("ERROR|")) {
                showAlert("Ошибка", response.substring(4));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при добавлении записи", e);
            showAlert("Ошибка", "Ошибка при добавлении записи: " + e.getMessage());
        }
    };
    private void loadData() {
        logger.info("Запрос данных заказов");
        out.println("GET");
        try {
            String response = in.readLine();
            order.clear();
            if (!response.isEmpty()) {
                String[] userEntries = response.split(";");
                for (String entry : userEntries) {
                    String[] fields = entry.split(",");
                    if (fields.length == 6) {
                        int table = Integer.parseInt(fields[0]);
                        String name = fields[1];
                        String phone = fields[2];
                        String date = fields[3];
                        String time = fields[4];
                        String wishes = fields[5];
                        order.add(new orderData(table, name, phone, date, time, wishes));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Communication Error", "Ошибка связи с сервером.");
        }
    }

    public void orderShowListData() {
        logger.info("Инициализация таблицы");
        //инициализация
        orderColtableid.setCellValueFactory(new PropertyValueFactory<>("table"));
        orderColname.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderColphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        orderColdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderColtime.setCellValueFactory(new PropertyValueFactory<>("time"));
        orderColwishes.setCellValueFactory(new PropertyValueFactory<>("wishes"));
        orderTableView.setItems(order);
    }

    @FXML
    private void onClear() {
        logger.info("Сброс значений в полях ввода");
        voidname.clear();
        voidphone.clear();
        voiddate.clear();
        voidtime.clear();
        voidwishes.clear();
    };
    @FXML
    private void onUpdate() {
        logger.info("Попытка обновлении записи в таблице");
        orderData selected = orderTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Выберите запись для обновления.");
            return;
        }

        String table = takedTable.getText();
        String name = voidname.getText();
        String phone = voidphone.getText();
        String date = voiddate.getText();
        String time = voidtime.getText();
        String wishes = voidwishes.getText();

        if (table.isEmpty() || name.isEmpty() ||phone.isEmpty() || date.isEmpty() ||
                time.isEmpty() || wishes.isEmpty()) {
            showAlert("Внимание", "Пожалуйста, заполните все поля.");
            return;
        }
    }

    @FXML
    public void tableButtonAction() {
        if (pane1.isVisible()){
            loadData();
            orderShowListData();
            pane1.setVisible(false);
        }
        else if(!pane1.isVisible()){
            pane1.setVisible(true);
        }
    }
}


//    private final ObservableList<Reservation> data = FXCollections.observableArrayList();
//    public void voidbutton() {
//        String name = voidname.getText();
//        String date = voiddate.getText();
//        String time = voidtime.getText();
//        String phone = voidphone.getText();
//
//        if (!name.isEmpty() && !date.isEmpty() && !time.isEmpty() &&!phone.isEmpty()) {
//            data.add(new Reservation(name, date, time, phone));
//            voidname.clear();
//            voiddate.clear();
//            voidtime.clear();
//            voidphone.clear();
//            takedTable.clear();
//        }
//
//    }
//
//    public class TableController {
//        @FXML
//        private TableView<Reservation> tableView;
//        @FXML
//        private TableColumn<Reservation, String> nameColumn;
//        @FXML
//        private TableColumn<Reservation, String> dateColumn;
//        @FXML
//        private TableColumn<Reservation, String> timeColumn;
//        @FXML
//        private TableColumn<Reservation, String> phoneColumn;
//
//
//
//
//        @FXML
//        private void initialize() {
//            nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
//            timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
//            phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
//
//            tableView.setItems(data);
//        }
//    }
//    }
