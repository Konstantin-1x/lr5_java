package com.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class SmartphoneApp extends Application {
    private ObservableList<Smartphone> smartphoneList = FXCollections.observableArrayList();
    private StackedBarChart<String, Number> memoryChart;

    private TableView<Smartphone> tableView;
    private TextField modelField, screenSizeField, screenTypeField, memoryField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Инициализация компонентов
        tableView = new TableView<>();
        modelField = new TextField();
        screenSizeField = new TextField();
        screenTypeField = new TextField();
        memoryField = new TextField();

        TableColumn<Smartphone, String> modelColumn = new TableColumn<>("Модель");
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());

        TableColumn<Smartphone, Double> screenSizeColumn = new TableColumn<>("Размер экрана");
        screenSizeColumn.setCellValueFactory(cellData -> cellData.getValue().screenSizeProperty().asObject());

        TableColumn<Smartphone, String> screenTypeColumn = new TableColumn<>("Тип экрана");
        screenTypeColumn.setCellValueFactory(cellData -> cellData.getValue().screenTypeProperty());

        TableColumn<Smartphone, Double> memoryColumn = new TableColumn<>("Объем памяти");
        memoryColumn.setCellValueFactory(cellData -> cellData.getValue().memoryProperty().asObject());

        tableView.getColumns().addAll(modelColumn, screenSizeColumn, screenTypeColumn, memoryColumn);

        // Создание графика StackedBarChart
        memoryChart = createStackedBarChart();

        // Панель ввода данных
        HBox inputPanel = createInputPanel();

        // Кнопки действий
        HBox actionPanel = createActionPanel();

        // Панель кнопок для сохранения и загрузки
        HBox filePanel = createFilePanel(primaryStage);

        VBox root = new VBox(10, tableView, memoryChart, inputPanel, actionPanel, filePanel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Смартфоны");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createInputPanel() {
        HBox inputPanel = new HBox(10);
        inputPanel.getChildren().addAll(
                new Label("Модель:"), modelField,
                new Label("Размер экрана:"), screenSizeField,
                new Label("Тип экрана:"), screenTypeField,
                new Label("Объем памяти:"), memoryField
        );
        return inputPanel;
    }

    private HBox createActionPanel() {
        Button addButton = new Button("Добавить");
        addButton.setOnAction(event -> addSmartphone());

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(event -> deleteSmartphone());

        Button editButton = new Button("Редактировать");
        editButton.setOnAction(event -> editSmartphone());

        HBox actionPanel = new HBox(10, addButton, deleteButton, editButton);
        actionPanel.setAlignment(Pos.CENTER);
        return actionPanel;
    }

    private HBox createFilePanel(Stage primaryStage) {
        Button saveButton = new Button("Сохранить в файл");
        saveButton.setOnAction(event -> saveToFile());

        Button loadButton = new Button("Загрузить из файла");
        loadButton.setOnAction(event -> loadFromFile(primaryStage));

        HBox filePanel = new HBox(10, saveButton, loadButton);
        filePanel.setAlignment(Pos.CENTER);
        return filePanel;
    }

    private void addSmartphone() {
        String model = modelField.getText();
        double screenSize = Double.parseDouble(screenSizeField.getText());
        String screenType = screenTypeField.getText();
        double memory = Double.parseDouble(memoryField.getText());

        Smartphone smartphone = new Smartphone(model, screenSize, screenType, memory);
        smartphoneList.add(smartphone);
        tableView.setItems(smartphoneList);
        updateChart(); // Обновление графика при добавлении нового смартфона
    }

    private void deleteSmartphone() {
        Smartphone selectedSmartphone = tableView.getSelectionModel().getSelectedItem();
        if (selectedSmartphone != null) {
            smartphoneList.remove(selectedSmartphone);
            updateChart(); // Обновление графика при удалении записи
        }
    }

    private void editSmartphone() {
        Smartphone selectedSmartphone = tableView.getSelectionModel().getSelectedItem();
        if (selectedSmartphone != null) {
            selectedSmartphone.setModel(modelField.getText());
            selectedSmartphone.setScreenSize(Double.parseDouble(screenSizeField.getText()));
            selectedSmartphone.setScreenType(screenTypeField.getText());
            selectedSmartphone.setMemory(Double.parseDouble(memoryField.getText()));
            updateChart(); // Обновление графика при изменении записи
        }
    }

    private StackedBarChart<String, Number> createStackedBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> memorySeries = new XYChart.Series<>();
        memorySeries.setName("Объем памяти");

        // Добавление данных в график (изначально пусто)
        chart.getData().add(memorySeries);
        return chart;
    }

    private void updateChart() {
        memoryChart.getData().clear(); // Очищаем текущие данные графика

        XYChart.Series<String, Number> memorySeries = new XYChart.Series<>();
        memorySeries.setName("Объем памяти");

        // Добавление новых данных из списка смартфонов
        for (Smartphone smartphone : smartphoneList) {
            memorySeries.getData().add(new XYChart.Data<>(smartphone.getModel(), smartphone.getMemory()));
        }

        memoryChart.getData().add(memorySeries); // Обновляем график
    }

    // Метод для сохранения данных в файл
    private void saveToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Smartphone smartphone : smartphoneList) {
                    writer.write(smartphone.getModel() + "," +
                            smartphone.getScreenSize() + "," +
                            smartphone.getScreenType() + "," +
                            smartphone.getMemory());
                    writer.newLine();
                }
                System.out.println("Данные сохранены в файл.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод для загрузки данных из файла
    private void loadFromFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                smartphoneList.clear(); // Очищаем текущий список
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 4) {
                        String model = data[0];
                        double screenSize = Double.parseDouble(data[1]);
                        String screenType = data[2];
                        double memory = Double.parseDouble(data[3]);

                        smartphoneList.add(new Smartphone(model, screenSize, screenType, memory));
                    }
                }
                tableView.setItems(smartphoneList);
                updateChart(); // Обновление графика после загрузки данных
                System.out.println("Данные загружены из файла.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
