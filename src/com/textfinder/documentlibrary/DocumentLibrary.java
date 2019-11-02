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

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 * @version 1.8
 */
public class DocumentLibrary {
    /*
    cambiar a ruta dinamica

    version adrian
    public final static String PATH1 = "C:/Users/carlo/OneDrive/Escritorio/TEC 2019/Material de cursos/Datos 1/Proyectos/Proyecto 2/Proyecto_2_CE/src/com/textfinder/documentlibrary/documents/";
    public final static String PATH2 = "C:\\Users\\carlo\\OneDrive\\Escritorio\\TEC 2019\\Material de cursos\\Datos 1\\Proyectos\\Proyecto 2\\Proyecto_2_CE\\src\\com\\textfinder\\documentlibrary\\documents";
     */
    public final static String PATH1 = "C:/Users/danic/Documents/GitHub/Proyecto_2_CE/src/com/textfinder/documentlibrary/documents/";
    public static ArrayList<String> filesString = new ArrayList<String>();
    public static ArrayList<File> files = new ArrayList<File>();

    /**
     * Contructor
     */
    private DocumentLibrary(){}

    /**
     * Configura el filechooser
     * @param pFileChooser
     */
    private static void configFileChooser(FileChooser pFileChooser){
        pFileChooser.setTitle("Add file");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx)", "*.docx");
        pFileChooser.getExtensionFilters().add(txtFilter);
        pFileChooser.getExtensionFilters().add(pdfFilter);
        pFileChooser.getExtensionFilters().add(docxFilter);
    }

    /**
     * Copia el archivo a la biblioteca, retorna true si se realiza correctamente y false de lo contrario
     * @param file
     * @return boolean
     */
    private static boolean copyFiles(File file){

        updateFileList();

        try{
            if(!filesString.contains(file.getName())){
                Path o = Paths.get(file.getAbsolutePath());
                Path d = Paths.get(PATH1 + file.getName());
                Files.copy(o,d, StandardCopyOption.REPLACE_EXISTING);
                filesString.add(file.getName());
                files.add(file);
                return true;
            }else{
                return false;
            }

        }catch(IOException e){
            return false;
        }
    }

    /**
     * Agrega un archivo a la biblioteca de documentos
     * @param stage
     * @return boolean
     */
    public static boolean addFile(Stage stage) {

        //Instancia y configuracion del filechooser
        FileChooser fileChooser = new FileChooser();
        configFileChooser(fileChooser);

        //Muestra el filechooser
        File file = fileChooser.showOpenDialog(stage);
        //Copia el archivo
        if(file != null){
            if(copyFiles(file)){
                Dialogs.showInformationDialog("Success", "The file has been successfully added");
            }else{
                Dialogs.showErrorDialog("Failed", "The file you want to enter is already in the library");
                //AGREGAR OPCION DE REEMPLAZAR.
            }
            return true;
        }else {
            return false;
        }

    }

    /**
     * Agrega una carpeta de archivos a la biblioteca de documentos
     * @param stage
     * @return boolean
     */
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
                        filesNamesFailed += folder.list()[i] + "   ------>   it already exists\n";
                    }
                }else{
                    filesNamesFailed += folder.list()[i] + "   ------>   extension denied\n";
                }
            }
        }
        if(filesNamesSuccess != ""){
            finalText += "The following files have been successfully added: \n\n" + filesNamesSuccess + "\n";
        }
        if(filesNamesFailed != ""){
            finalText += "Due to an error the following files have not been added: \n\n" + filesNamesFailed + "\n";
        }
        Dialogs.showInformationDialog("Finish", finalText);
        return true;
    }

    /**
     * Elimina un documento de la lista de documentos
     * @param pFile
     * @return boolean
     */
    public static boolean deleteFile(String pFile){

        for(int i = 0; i < files.size(); i++){
            if(files.get(i).getName().equals(pFile)){
                File fichero = files.get(i);
                fichero.delete();
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza la lista de documentos de la biblioteca
     * @return boolean
     */
    public static boolean refreshFiles(){
        updateFileList();
        return true;
    }

    /**
     * Devuelve la extension del archivo pasado por parametro
     * @param file
     * @return String
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    /**
     * Actualiza la lista de documentos de la biblioteca
     */
    public static void updateFileList(){
        filesString = new ArrayList<String>();
        files = new ArrayList<File>();

        File directory = new File(PATH1);

        File directoryFile = new File(directory.getAbsolutePath()); // carpeta donde estan los pdf
        String[] listFiles = directoryFile.list();//extrae los nombres de archivo

        for(int i = 0; i < directoryFile.list().length; i++){
            filesString.add(listFiles[i]);
            files.add(directoryFile.listFiles()[i]);
        }
    }
}
