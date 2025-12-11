package com.vasilevyuv.crud_project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AddItemController {
    @FXML private VBox addItem;
    @FXML private Button addButton;
    
    private MainController mainController;
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    @FXML
    private void handleAddClick() {
        if (mainController != null) {
            mainController.handleAddEntity();
        }
    }
}