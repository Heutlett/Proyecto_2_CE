package com.textfinder.filemanagers;

import com.textfinder.structures.Dialogs;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 * @version 1.8
 */
public class DOCXManager {
    /**
     * Devuelve el texto de un archivo docx dividiendo las palabras solo por espacios
     * @param pPath
     * @return String
     */
    public static String getText(String pPath){

        try {

            XWPFDocument docx = new XWPFDocument(new FileInputStream(pPath));
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            return we.getText().replaceAll("\n", " ");

        }catch (IOException e){
            Dialogs.showErrorDialog("Failed", "File not found");
            return null;
        }

    }

    /**
     * Devuelve el texto plano de un archivo docx
     * @param pPath
     * @return String
     */
    public static String getPlainText(String pPath){

        try {
            XWPFDocument docx = new XWPFDocument(new FileInputStream(pPath));
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            return we.getText();

        }catch (IOException e){
            Dialogs.showErrorDialog("Failed", "File not found");
            return null;
        }
    }
}
