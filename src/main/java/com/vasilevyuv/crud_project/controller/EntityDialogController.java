package com.vasilevyuv.crud_project.controller;

import com.vasilevyuv.crud_project.model.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EntityDialogController {
    @FXML private VBox dialogPane;
    @FXML private Label dialogTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private Label nameError;
    @FXML private Label descriptionError;
    @FXML private Button actionButton;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    
    private Stage dialogStage;
    private MainController mainController;
    private Entity entity; // null для создания, не null для редактирования
    private DialogCallback callback;
    
    // Константы для валидации
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        
        // Закрытие по ESC
        dialogStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    handleClose();
                    break;
                case ENTER:
                    if (!actionButton.isDisable()) {
                        handleAction();
                    }
                    break;
            }
        });
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity != null) {
            // Режим редактирования
            dialogTitle.setText("Edit Entity");
            actionButton.setText("Update");
            
            nameField.setText(entity.getName());
            descriptionField.setText(entity.getDescription());
        } else {
            // Режим создания
            dialogTitle.setText("Add Entity");
            actionButton.setText("Create");
        }
    }
    
    @FXML
    private void initialize() {
        // Валидация в реальном времени
        nameField.textProperty().addListener((observable, oldValue, newValue) -> validateName());
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> validateDescription());
        
        // Изначально кнопка отключена
        actionButton.setDisable(true);
    }
    
    private void validateName() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            showError(nameError, "Name is required");
            actionButton.setDisable(true);
        } else if (name.length() < MIN_NAME_LENGTH) {
            showError(nameError, "Name must be at least " + MIN_NAME_LENGTH + " characters");
            actionButton.setDisable(true);
        } else if (name.length() > MAX_NAME_LENGTH) {
            showError(nameError, "Name must be at most " + MAX_NAME_LENGTH + " characters");
            actionButton.setDisable(true);
        } else {
            hideError(nameError);
            validateAll();
        }
    }
    
    private void validateDescription() {
        String description = descriptionField.getText().trim();
        
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            showError(descriptionError, "Description must be at most " + MAX_DESCRIPTION_LENGTH + " characters");
            actionButton.setDisable(true);
        } else {
            hideError(descriptionError);
            validateAll();
        }
    }
    
    private boolean validateAll() {
        // Проверяем все поля
        boolean nameValid = !nameError.isVisible() && !nameField.getText().trim().isEmpty();
        boolean descriptionValid = !descriptionError.isVisible();
        
        actionButton.setDisable(!(nameValid && descriptionValid));
        return nameValid && descriptionValid;
    }
    
    private void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void hideError(Label errorLabel) {
        errorLabel.setVisible(false);
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }

    @FXML
    private void handleAction() {
        if (validateAll()) {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (entity == null) {
                // Создание новой сущности
                Entity newEntity = new Entity(name, description);
                if (callback != null) {
                    callback.onEntityCreated(newEntity);
                }
            } else {
                // Обновление существующей сущности
                entity.setName(name);
                entity.setDescription(description);
                if (callback != null) {
                    callback.onEntityUpdated(entity);
                }
            }

            dialogStage.close();
        }
    }

    @FXML
    private void handleClose() {
        if (callback != null) {
            callback.onDialogClosed();
        }
        dialogStage.close();
    }
}