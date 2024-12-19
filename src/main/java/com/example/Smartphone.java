package com.example;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class Smartphone {
    private final StringProperty model;
    private final DoubleProperty screenSize;
    private final StringProperty screenType;
    private final DoubleProperty memory;

    public Smartphone(String model, double screenSize, String screenType, double memory) {
        this.model = new SimpleStringProperty(model);
        this.screenSize = new SimpleDoubleProperty(screenSize);
        this.screenType = new SimpleStringProperty(screenType);
        this.memory = new SimpleDoubleProperty(memory);
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public Double getScreenSize() {
        return screenSize.get();
    }

    public void setScreenSize(Double screenSize) {
        this.screenSize.set(screenSize);
    }

    public String getScreenType() {
        return screenType.get();
    }

    public void setScreenType(String screenType) {
        this.screenType.set(screenType);
    }

    public Double getMemory() {
        return memory.get();
    }

    public void setMemory(Double memory) {
        this.memory.set(memory);
    }

    public StringProperty modelProperty() {
        return model;
    }

    public DoubleProperty screenSizeProperty() {
        return screenSize;
    }

    public StringProperty screenTypeProperty() {
        return screenType;
    }

    public DoubleProperty memoryProperty() {
        return memory;
    }
}
