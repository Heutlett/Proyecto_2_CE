package com.textfinder.documentlibrary;

import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class LeerPdf {

    public static void main(String[] args) {
        LeerPdf leerPDF = new LeerPdf();
        String directory = leerPDF.getDirectory();
        String label = JOptionPane.showInputDialog(null, "Busca:");
        leerPDF.readPDF(directory, label);
    }

    /**
     * Metodo que permite mandar un JFileChooser que es una ventanita para poder
     * escoger el directorio
     * @return la ruta especifica donde buscara el directorio
     */
    public String getDirectory() {
        String ruta = "";
        // componente de dialogo
        JFileChooser jfc = new JFileChooser();
        // configuramos para que solo sean directorios
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //mandamos el cuadro, esperamos una respuesta aprobatoria
        int dialogResult = jfc.showOpenDialog(null);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            ruta = jfc.getSelectedFile().getAbsolutePath();
        }
        System.out.println(ruta);
        return ruta;
    }

    /**
     * Metodo que permite abrir PDF y buscar si contiene una palabra Si la
     * contiene guarda en un archivo de texto el nombre del archivo
     *
     * @param directory la ruta absoluta del directorio que busca los PDF
     * @param label La etiqueta a buscar
     */
    public void readPDF(String directory, String label) {
        File directoryFile = new File(directory); // carpeta donde estan los pdf
        String[] listFiles = directoryFile.list();//extrae los nombres de archivo
        String slash = System.getProperty("file.separator");
        if (listFiles.length == 0)
            System.out.println("No hay archivos en la carpeta especificada");
        else {
            //creamos un flujo donde se pondran los nombres de los archivos que contienen los elementos
            PrintWriter pw =null;
            try {
                pw = new PrintWriter("destination.txt");
                //recorro cada uno de los elementos
                for (String file : listFiles) {
                    //preguntamos si es un pdf
                    if(!file.toLowerCase().endsWith(".pdf"))
                        continue;
                    //carga el archivo
                    PDDocument pdf = PDDocument.load(new File
                            (directory + slash  +file));
                    //extramos su contenido
                    String content = new PDFTextStripper()
                            .getText(pdf);
                    //cerramos el archivo
                    pdf.close();
                    //preguntamos si existe lo que se quiere buscar
                    if (content.contains(label)) {
                        //escribimos en el archivo de destino
                        pw.println(file);
                    }
                }
            } catch (Exception e) {
                System.out.println("Hay un error al abrir el archivo");
            } finally{
                pw.close();
            }
        }
    }
}


