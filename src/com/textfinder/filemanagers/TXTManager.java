package com.textfinder.filemanagers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TXTManager {

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
