package org.example.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {

    private final StringProperty name;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty phone;

    public Reservation(String name, String date, String time,String phone) {
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.phone = new SimpleStringProperty(phone);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }
    public StringProperty phoneProperty() {
        return phone;
    }

    public String getName() {
        return name.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }
    public String getPhone() {
        return phone.get();
    }
}
