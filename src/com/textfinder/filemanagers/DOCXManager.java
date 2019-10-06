package com.textfinder.filemanagers;

import com.textfinder.structures.Dialogs;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;

public class DOCXManager {

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
