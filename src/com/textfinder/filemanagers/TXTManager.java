package com.textfinder.filemanagers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Adrian Araya Ramirez
 * @autor Daniel Quesada Camacho
 * @version 1.8
 */
public class TXTManager {
    /**
     * Devuelve el texto de un archivo txt dividiendo las palabras solo por espacios
     * @param pPath
     * @return String
     */
     public static String getText(String pPath){

         try {
             Scanner file = new Scanner(new File(pPath));
             String text = "";

             while (file.hasNext()){
                 text += file.next() + " ";
             }
             return text;

         }catch (IOException e){
             return null;
         }
     }

    /**
     * Devuelve el texto plano de un archivo txt
     * @param pPath
     * @return String
     */
     public static String getPlainText(String pPath){

         try {
             Scanner file = new Scanner(new File(pPath));
             String text = "";

             while (file.hasNext()){
                 text += file.nextLine() + "\n";
             }
             return text;

         }catch (IOException e){
             return null;
         }
     }
}
