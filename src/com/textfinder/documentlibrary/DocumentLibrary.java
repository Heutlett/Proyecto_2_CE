package com.textfinder.documentlibrary;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DocumentLibrary {

    private final static String PATH = "C:/Users/carlo/OneDrive/Escritorio/TEC 2019/Material de cursos/Datos 1/Proyectos/Proyecto 2/Proyecto_2_CE/src/com/textfinder/documentlibrary/";

    private DocumentLibrary(){}

    public static boolean addFile(Stage stage) {

        try{

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);

            Path o = Paths.get(file.getAbsolutePath());

            Path d = Paths.get(PATH + file.getName());

            Files.copy(o,d, StandardCopyOption.REPLACE_EXISTING);

            return true;

        }catch(IOException e){
            JOptionPane.showConfirmDialog(null, "Hubo un error en la localizacion del archivo");
            return false;
        }
    }

    public static boolean addFolder(){
        return true;
    }

    public static boolean deleteFile(String pFile){
        return true;
    }

    public static boolean updateFile(String pFile){
        return true;
    }

    public static boolean openFile(String pFile){
        return true;
    }

}
