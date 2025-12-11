package com.vasilevyuv.crud_project.util;

import com.vasilevyuv.crud_project.controller.DialogCallback;
import com.vasilevyuv.crud_project.controller.EntityDialogController;
import com.vasilevyuv.crud_project.model.Entity;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtil {

    public static void showEntityDialog(Entity entity, Stage parentStage, DialogCallback callback) {
        try {
            StackPane overlay = new StackPane();

            FXMLLoader loader = new FXMLLoader(
                    DialogUtil.class.getResource("/com/vasilevyuv/crud_project/scenes/entity-dialog.fxml")
            );
            Parent dialogRoot = loader.load();

            EntityDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);

            overlay.getChildren().add(dialogRoot);
            Scene scene = new Scene(overlay, 500, 400, Color.rgb(59,59,59));

            scene.getStylesheets().addAll(
                    Objects.requireNonNull(DialogUtil.class.getResource("/com/vasilevyuv/crud_project/styles/entities.css")).toExternalForm(),
                    Objects.requireNonNull(DialogUtil.class.getResource("/com/vasilevyuv/crud_project/static/fonts/fonts.css")).toExternalForm()
            );

            dialogStage.setScene(scene);

            controller.setDialogStage(dialogStage);
            controller.setEntity(entity);
            controller.setCallback(callback);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}