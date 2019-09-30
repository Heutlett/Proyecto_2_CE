package com.textfinder.structures;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Dialogs {

    private Dialogs(){}

    public static void showInformationDialog(String pHeaderText, String pContentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(pHeaderText);
        alert.setContentText(pContentText);

        alert.showAndWait();
    }

    public static void showWarningDialog(String pHeaderText, String pContentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(pHeaderText);
        alert.setContentText(pContentText);

        alert.showAndWait();
    }

    public static void showErrorDialog(String pHeaderText, String pContentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(pHeaderText);
        alert.setContentText(pContentText);

        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String pHeaderText, String pContentText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public static String showInputDialog(String pHeaderText, String pContentText){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText(pHeaderText);
        dialog.setContentText(pContentText);

        Optional<String> result = dialog.showAndWait();

        return result.get();
    }

}
