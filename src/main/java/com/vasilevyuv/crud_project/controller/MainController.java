package com.vasilevyuv.crud_project.controller;

import com.vasilevyuv.crud_project.model.Entity;
import com.vasilevyuv.crud_project.service.EntityService;
import com.vasilevyuv.crud_project.util.DialogUtil;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController implements DialogCallback {

    // FXML элементы
    @FXML private ScrollPane scrollPane;
    @FXML private VBox entitiesContainer;
    @FXML private TextField searchField;
    @FXML private Button clearButton;
    @FXML private Label countLabel;
    @FXML private Pagination pagination;

    private EntityService entityService;
    private Stage stage;

    // Настройки пагинации
    private static final int PAGE_SIZE = 10;
    private int currentPage = 1;
    private String currentSearchTerm = "";
    private int totalItems = 0;

    @FXML
    public void initialize() {
        entityService = new EntityService();
        setupListeners();
        loadEntities();
    }

    private void setupListeners() {
        // Поиск при нажатии Enter в поле поиска
        searchField.setOnAction(event -> performSearch());

        // Кнопка очистки
        clearButton.setOnAction(event -> {
            searchField.clear();
            currentSearchTerm = "";
            currentPage = 1;
            loadEntities();
        });

        // Пагинация
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = newVal.intValue() + 1;
            loadEntities();
        });

        // Live search (поиск с задержкой)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                Platform.runLater(() -> {
                    currentSearchTerm = newValue.trim();
                    currentPage = 1;
                    loadEntities();
                });
            }
        });
    }

    private void performSearch() {
        currentSearchTerm = searchField.getText().trim();
        currentPage = 1;
        loadEntities();
    }

    private void loadEntities() {
        try {
            entitiesContainer.getChildren().clear();

            // Получаем данные с учетом поиска и пагинации
            List<Entity> entities;
            if (currentSearchTerm.isEmpty()) {
                totalItems = entityService.getTotalCount();
                entities = entityService.getEntities(currentPage, PAGE_SIZE);
            } else {
                totalItems = entityService.getSearchCount(currentSearchTerm);
                entities = entityService.searchEntities(currentSearchTerm, currentPage, PAGE_SIZE);
            }

            // Обновляем пагинацию
            int pageCount = (int) Math.ceil((double) (totalItems + 1) / PAGE_SIZE);
            pagination.setPageCount(Math.max(1, pageCount));
            pagination.setCurrentPageIndex(currentPage - 1);

            // Добавляем сущности
            for (Entity entity : entities) {
                addEntityItem(entity);
            }

            // Добавляем кнопку "Добавить" если есть место на странице
            if (entities.size() < PAGE_SIZE) {
                addAddButton();
            }

            // Обновляем счетчик
            countLabel.setText(String.format("Found: %d | Page: %d/%d",
                    totalItems, currentPage, Math.max(1, pageCount)));

        } catch (Exception e) {
            showError("Error loading entities", e.getMessage());
        }
    }

    private void addEntityItem(Entity entity) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/vasilevyuv/crud_project/scenes/entity-item.fxml")
            );

            VBox entityItem = loader.load();
            EntityItemController controller = loader.getController();
            controller.setEntity(entity);
            controller.setMainController(this);

            entitiesContainer.getChildren().add(entityItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/vasilevyuv/crud_project/scenes/add-item.fxml")
            );

            VBox addItem = loader.load();
            AddItemController controller = loader.getController();
            controller.setMainController(this);

            entitiesContainer.getChildren().add(addItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleAddEntity() {
        DialogUtil.showEntityDialog(null, stage, this);
    }

    public void handleUpdateEntity(Entity entity) {
        DialogUtil.showEntityDialog(entity, stage, this);
    }

    public void handleDeleteEntity(Entity entity) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Entity");
        alert.setContentText("Are you sure you want to delete '" + entity.getName() + "'?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteEntity(entity);
            }
        });
    }

    @Override
    public void onEntityCreated(Entity entity) {
        try {
            entityService.createEntity(entity.getName(), entity.getDescription());
            refreshEntities();
            showSuccess("Entity created successfully");
        } catch (Exception e) {
            showError("Error creating entity", e.getMessage());
        }
    }

    @Override
    public void onEntityUpdated(Entity entity) {
        try {
            entityService.updateEntity(entity.getId(), entity.getName(), entity.getDescription());
            refreshEntities();
            showSuccess("Entity updated successfully");
        } catch (Exception e) {
            showError("Error updating entity", e.getMessage());
        }
    }

    @Override
    public void onDialogClosed() {}

    private void deleteEntity(Entity entity) {
        try {
            if (entityService.deleteEntity(entity.getId())) {
                refreshEntities();
                showSuccess("Entity deleted successfully");
            } else {
                showError("Error", "Failed to delete entity");
            }
        } catch (Exception e) {
            showError("Error deleting entity", e.getMessage());
        }
    }

    public void refreshEntities() {
        loadEntities();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}