package com.textfinder.filemanagers;

import java.io.File;
import java.io.IOException;
import com.textfinder.structures.Dialogs;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 * @version 1.8
 */
public class PDFManager {

    private static PDFParser parser;
    private static PDFTextStripper pdfStripper;
    private static PDDocument pdDoc;
    private static COSDocument cosDoc;
    private static String Text;
    private static String filePath;
    private static File file;
    private static String words = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890 ";

    /**
     * Constructor
     */
    private PDFManager() {

    }

    /**
     * Devuelve el texto de un archivo pdf dividiendo las palabras solo por espacios
     * @param pFilePath
     * @return String
     */
    public static String toText(String pFilePath){

        try{
            pdfStripper = null;
            pdDoc = null;
            cosDoc = null;
            file = new File(pFilePath);
            parser = new PDFParser(new RandomAccessFile(file, "r")); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(0);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());
            Text = pdfStripper.getText(pdDoc);

            String textFinal = "";

            for(int i = 0; i < Text.length(); i++){
                if(words.contains("" +Text.charAt(i))){
                    textFinal += Text.charAt(i);
                }
            }
            return textFinal;
        }catch (IOException e){
            Dialogs.showErrorDialog("Failed", "File not found");
            return null;
        }

    }

    /**
     * Devuelve el texto plano de un archivo txt
     * @param pFilePath
     * @return
     */
    public static String getPlainText(String pFilePath) {
        try{
            pdfStripper = null;
            pdDoc = null;
            cosDoc = null;
            file = new File(pFilePath);
            parser = new PDFParser(new RandomAccessFile(file, "r")); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(0);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());
            Text = pdfStripper.getText(pdDoc);
            return Text;
        }catch (IOException e){
            Dialogs.showErrorDialog("Failed", "File not found");
            return null;
        }
    }
}