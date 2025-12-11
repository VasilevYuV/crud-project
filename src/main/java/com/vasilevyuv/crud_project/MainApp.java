package com.vasilevyuv.crud_project;

import com.vasilevyuv.crud_project.config.DatabaseInitializer;
import com.vasilevyuv.crud_project.controller.MainController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        initializeDatabase();
        launch(args);
    }

    private static void initializeDatabase() {
        try {
            // Проверяем подключение к БД
            DatabaseInitializer.checkDatabaseConnection();

            // Инициализируем таблицы
            DatabaseInitializer.initializeDatabase();

            System.out.println("Database setup completed successfully");

        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize database: " + e.getMessage());
            System.err.println("Application cannot start without database.");
            System.err.println("Please check:");
            System.err.println("1. PostgreSQL is running");
            System.err.println("2. Database 'crud_project' exists");
            System.err.println("3. Connection settings in .env file are correct");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            // Загружаем FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/vasilevyuv/crud_project/scenes/main-app-with-controls.fxml")
            );
            AnchorPane root = loader.load();

            // Получаем контроллер и передаем Stage
            MainController controller = loader.getController();
            controller.setStage(stage);

            // Создаем сцену
            Scene scene = new Scene(root, 640, 500, Color.rgb(59,59,59));

            // Загружаем стили
            scene.getStylesheets().addAll(
                    getClass().getResource("/com/vasilevyuv/crud_project/styles/main-app.css").toExternalForm(),
                    getClass().getResource("/com/vasilevyuv/crud_project/styles/entities.css").toExternalForm(),
                    getClass().getResource("/com/vasilevyuv/crud_project/static/fonts/fonts.css").toExternalForm()
            );

            // Настраиваем Stage
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("CRUD Project");
            stage.show();

            System.out.println("Application started successfully");

        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}