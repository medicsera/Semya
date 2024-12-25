package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

class orderHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(orderHandler.class.getName());

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Connection connect;

    public orderHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //устанавливаем соединение с базой данных
            connect = database.connectBd();
            logger.info("Подключен клиент: " + socket.getInetAddress());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при подключении клиента", e);
        }
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
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());

            //добавление обработчиков к логгеру
            logger.addHandler(consoleHandler);
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
            // Если логирование не удалось настроить, продолжаем без него
        }
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = in.readLine()) != null) {
                logger.info("Получен запрос: " + request);
                String[] parts = request.split("\\|");
                String command = parts[0];

                switch (command) {
                    case "GET":
                        handleGet();
                        break;
                    case "ADD":
                        handleAdd(parts);
                        break;
                    case "DELETE":
                        handleDelete(parts);
                        break;
                    default:
                        out.println("UNKNOWN_COMMAND");
                        logger.warning("Неизвестная команда: " + command);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при обработке запроса клиента", e);
        } finally {
            try {
                connect.close();
                socket.close();
                logger.info("Клиент отключился: " + socket.getInetAddress());
            } catch (IOException | SQLException e) {
                logger.log(Level.SEVERE, "Ошибка при закрытии соединения", e);
            }
        }
    }

    private void handleGet() {
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tableid, name, phone, date, time, wishes FROM order");
            List<String> orders = new ArrayList<>();
            while (rs.next()) {

                int tableid = rs.getInt("tableid");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                java.sql.Date date = rs.getDate("date");
                String time = rs.getString("time");
                String wishes = rs.getString("wishes");


                orders.add(tableid+ "," +
                        phone + "," +
                        date + "," +
                        time + "," +
                        wishes + "," +
                        name + ","
                );
            }
            logger.info("Отправка данных заказа студенту");
            out.println(String.join(";", orders));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при выполнении handleGet", e);
            out.println("ERROR|" + e.getMessage());
        }
    }

    private void handleAdd(String[] parts) {
        try {
            String query = "INSERT INTO orders (tableid,name, phone, date, time, wishes) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = connect.prepareStatement(query);
            pstmt.setString(1, parts[1]);
            pstmt.setString(2, parts[2]);
            pstmt.setString(3, parts[3]);
            pstmt.setString(4, parts[4]);
            pstmt.setString(5, parts[5]);
            pstmt.setString(6, parts[6]);
            pstmt.executeUpdate();
            out.println("SUCCESS|Запись добавлена");
            logger.info("Добавлена новая запись: ");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при добавлении записи", e);
            out.println("ERROR|" + e.getMessage());
        }
    }

    private void handleDelete(String[] parts) {
        try {
            String query = "DELETE FROM student WHERE tableid=?";
            PreparedStatement pstmt = connect.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(parts[1]));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                out.println("SUCCESS|Запись удалена");
                logger.info("Запись удалена");
            } else {
                out.println("ERROR|Запись не найдена");
                logger.info("Запись не найдена");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении записи", e);
            out.println("ERROR|" + e.getMessage());
        }
    }

}