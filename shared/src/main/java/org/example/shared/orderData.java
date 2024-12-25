package org.example.shared;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class orderData {
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty wishes;
    private final IntegerProperty tableid;

    public orderData(Integer tableid, String name, String phone, String date, String time, String wishes) {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.date = new SimpleStringProperty(date.toString());
        this.time = new SimpleStringProperty(time.toString());
        this.wishes = new SimpleStringProperty(wishes);
        this.tableid = new SimpleIntegerProperty(tableid);
    }

    public Integer getTableid() {
        return tableid.get();
    }

    public IntegerProperty tableidProperty() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid.set(tableid);
    }


    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }


    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }


    public String getPhone() {
        return phone.get();
    }

    public StringProperty proneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }


    public String getWishes() {
        return wishes.get();
    }

    public StringProperty wishesProperty() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes.set(wishes);
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}