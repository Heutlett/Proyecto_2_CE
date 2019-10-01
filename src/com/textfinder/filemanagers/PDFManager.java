package com.textfinder.filemanagers;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFManager {

    private static PDFParser parser;
    private static PDFTextStripper pdfStripper;
    private static PDDocument pdDoc;
    private static COSDocument cosDoc;

    private static String Text;
    private static String filePath;
    private static File file;

    private PDFManager() {

    }

    public static String toText(String pFilePath) throws IOException {
        filePath = pFilePath;
        pdfStripper = null;
        pdDoc = null;
        cosDoc = null;

        file = new File(filePath);
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
    }

    public PDDocument getPdDoc() {
        return pdDoc;
    }


}