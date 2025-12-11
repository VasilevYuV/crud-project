package com.vasilevyuv.crud_project.controller;

import com.vasilevyuv.crud_project.model.Entity;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EntityItemController {
    @FXML private VBox entityItem;
    @FXML private HBox mainContentHBox;
    @FXML private VBox contentVBox;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label createdAtLabel;
    @FXML private Label updatedAtLabel;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private Entity entity;
    private MainController mainController;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public void setEntity(Entity entity) {
        this.entity = entity;
        updateUI();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void updateUI() {
        if (entity == null) return;

        String shortId = entity.getId().toString().substring(0, 8);
        idLabel.setText(shortId);

        nameLabel.setText(entity.getName());
        if (entity.getDescription() == null || entity.getDescription().isEmpty()) {
            descriptionLabel.setVisible(false);
            nameLabel.setPrefHeight(120);
            nameLabel.setStyle("-fx-font-size: 18px");
            contentVBox.setAlignment(Pos.CENTER_LEFT);
        } else {
            descriptionLabel.setVisible(true);
            descriptionLabel.setManaged(true);
            descriptionLabel.setText(entity.getDescription());
            contentVBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        }

        createdAtLabel.setText("Created: " + entity.getCreatedAt().format(DATE_FORMATTER));
        updatedAtLabel.setText("Updated: " + entity.getUpdatedAt().format(DATE_FORMATTER));
    }

    @FXML
    private void handleUpdateClick() {
        if (mainController != null && entity != null) {
            mainController.handleUpdateEntity(entity);
        }
    }

    @FXML
    private void handleDeleteClick() {
        if (mainController != null && entity != null) {
            mainController.handleDeleteEntity(entity);
        }
    }
}