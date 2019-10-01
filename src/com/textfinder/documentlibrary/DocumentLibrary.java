package com.textfinder.documentlibrary;
import com.textfinder.structures.Dialogs;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class DocumentLibrary {

    private final static String PATH1 = "C:/Users/carlo/OneDrive/Escritorio/TEC 2019/Material de cursos/Datos 1/Proyectos/Proyecto 2/Proyecto_2_CE/src/com/textfinder/documentlibrary/documents/";
    private final static String PATH2 = "C:\\Users\\carlo\\OneDrive\\Escritorio\\TEC 2019\\Material de cursos\\Datos 1\\Proyectos\\Proyecto 2\\Proyecto_2_CE\\src\\com\\textfinder\\documentlibrary\\documents";
    private static ArrayList<String> files = new ArrayList<>();

    private DocumentLibrary(){}

    public static ArrayList<String> getFileList(String pPath){
        File directoryFile = new File(pPath); // carpeta donde estan los pdf
        String[] listFiles = directoryFile.list();//extrae los nombres de archivo
        ArrayList<String> fileList = new ArrayList<String>();

        for(int i = 0; i < directoryFile.list().length; i++){
            fileList.add(listFiles[i]);
        }

        return fileList;
    }

    private static void configFileChooser(FileChooser pFileChooser){
        pFileChooser.setTitle("Add file");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx)", "*.docx");
        pFileChooser.getExtensionFilters().add(txtFilter);
        pFileChooser.getExtensionFilters().add(pdfFilter);
        pFileChooser.getExtensionFilters().add(docxFilter);
    }

    private static boolean copyFiles(File file){

        updateFileList();

        try{
            if(!files.contains(file.getName())){
                Path o = Paths.get(file.getAbsolutePath());
                Path d = Paths.get(PATH1 + file.getName());
                Files.copy(o,d, StandardCopyOption.REPLACE_EXISTING);
                files.add(file.getName());
                return true;
            }else{
                return false;
            }

        }catch(IOException e){
            return false;
        }
    }

    public static boolean addFile(Stage stage) {

        //Instancia y configuracion del filechooser
        FileChooser fileChooser = new FileChooser();
        configFileChooser(fileChooser);

        //Muestra el filechooser
        File file = fileChooser.showOpenDialog(stage);
        //Copia el archivo
        if(file != null){
            if(copyFiles(file)){
                Dialogs.showInformationDialog("Success", "El archivo se ha agregado satisfactoriamente");
            }else{
                Dialogs.showErrorDialog("Failed", "El archivo que desea ingresar ya se encuentra en la biblioteca");
                //AGREGAR OPCION DE REEMPLAZAR.
            }


            return true;
        }else {
            return false;
        }

    }

    public static boolean addFolder(Stage stage){

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Add folder");
        File folder = chooser.showDialog(stage);
        String filesNamesSuccess = "";
        String filesNamesFailed = "";
        String finalText = "";
        
        if(folder != null){

            for(int i = 0; i < folder.list().length; i++){

                if(getFileExtension(folder.listFiles()[i]).equals("txt")
                || getFileExtension(folder.listFiles()[i]).equals("pdf")
                || getFileExtension(folder.listFiles()[i]).equals("docx") ){
                    if(copyFiles(folder.listFiles()[i])){
                        filesNamesSuccess += folder.list()[i] + "\n";
                    }else{
                        filesNamesFailed += folder.list()[i] + "   ------>   ya existe\n";
                    }
                }else{
                    filesNamesFailed += folder.list()[i] + "   ------>   extension denegada\n";
                }

            }

        }
        if(filesNamesSuccess != ""){
            finalText += "Se han agregado satisfactoriamente los siguientes archivos: \n\n" + filesNamesSuccess + "\n";
        }

        if(filesNamesFailed != ""){
            finalText += "Debido a un error no se han agregado los siguientes archivos: \n\n" + filesNamesFailed + "\n";
        }
        Dialogs.showInformationDialog("Finish", finalText);
        return true;
    }

    public static boolean deleteFiles(String pFile){



        return true;
    }

    public static boolean refreshFiles(String pFile){
        return true;
    }

    public static boolean openFile(String pFile){
        return true;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private static void updateFileList(){
        files = new ArrayList<String>();
        File directoryFile = new File(PATH2); // carpeta donde estan los pdf
        String[] listFiles = directoryFile.list();//extrae los nombres de archivo

        for(int i = 0; i < directoryFile.list().length; i++){
            files.add(listFiles[i]);
        }
    }

    private void printFileList(){
        for(int i = 0; i < files.size(); i++){
            System.out.println(files.get(i));
        }
    }

}
