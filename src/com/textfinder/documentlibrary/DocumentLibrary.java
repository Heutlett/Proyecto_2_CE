package com.textfinder.documentlibrary;
import com.textfinder.structures.Dialogs;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DocumentLibrary {

    private final static String PATH = "C:/Users/carlo/OneDrive/Escritorio/TEC 2019/Material de cursos/Datos 1/Proyectos/Proyecto 2/Proyecto_2_CE/src/com/textfinder/documentlibrary/";

    private DocumentLibrary(){}


    private static void configFileChooser(FileChooser pFileChooser){
        pFileChooser.setTitle("Add file");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx)", "*.docx");
        pFileChooser.getExtensionFilters().add(txtFilter);
        pFileChooser.getExtensionFilters().add(pdfFilter);
        pFileChooser.getExtensionFilters().add(docxFilter);
    }

    public static boolean addFile(Stage stage) {

        try{
            //Instancia y configuracion del filechooser
            FileChooser fileChooser = new FileChooser();
            configFileChooser(fileChooser);

            //Muestra el filechooser
            File file = fileChooser.showOpenDialog(stage);
            //Copia el archivo
            if(file != null){
                Path o = Paths.get(file.getAbsolutePath());
                Path d = Paths.get(PATH + file.getName());
                Files.copy(o,d, StandardCopyOption.REPLACE_EXISTING);
                Dialogs.showInformationDialog("Success", "El archivo se ha agregado satisfactoriamente");
                return true;
            }else {
                return false;
            }
        }catch(IOException e){ //Lanza una excepcion en caso de haber problemas con el archivo.
            Dialogs.showErrorDialog("Failed", "Hubo un error en la localizacion del archivo");
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
