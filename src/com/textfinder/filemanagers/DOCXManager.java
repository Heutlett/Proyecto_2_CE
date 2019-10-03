package com.textfinder.filemanagers;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;

public class DOCXManager {

    public static String getText(String pPath){

        try {

            String path = "";

            for(int i = 0; i < pPath.length(); i++){
                if(pPath.charAt(i) == '\\'){
                    path +=  '/';
                }else{
                    path += pPath.charAt(i);
                }
            }


            System.out.println(path);

            XWPFDocument docx = new XWPFDocument(new FileInputStream(path));
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            return we.getText();

        }catch (IOException e){
            return null;
        }


    }


}
