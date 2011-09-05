package JavaO;

/**
 *
 * @author nikita
 */

import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
        
        
public class Text {
    
    static final int TabSize = 3;
    //static final char CharSpace = ' ';
    //static final char CharTab = '\t';
    //static final char CharEOL = '\n';
    //static final char CharEOT = '\0';
    static final int CharSpace = ' ';// 32
    static final int CharTab = '\t';//  9
    static final int CharEOL = '\n';//  10
    static final int CharEOT = '\0';//  0
    
    static boolean Ok = false;
    static String Message = "File not opened!";
    static int CurrentChar = CharEOT;
    static int CurrentCharIndex = -1;
    static byte[] SourceCode;
    
    private static InputStream InputFile;
    
    
    static void NextChar(){
        //try{
            if(SourceCode.length <= CurrentCharIndex+1){
                CurrentChar = CharEOT;
            }
            else {
                CurrentChar = SourceCode[++CurrentCharIndex];
                
                if(CurrentChar == CharEOL){
                    Location.Line++;
                    Location.Pos = 0;
                }
                else if(CurrentChar == '\r'){
                    NextChar();
                } 
                else if(CurrentChar != CharTab){
                    Location.Pos++;
                }
                else {
                    while(++Location.Pos % TabSize != 0);
                }
            }
        //}
        //catch(IOException e){};
    }
    
    
    static void reset(){
        /*if(Location.Path == null){
            System.out.println("Call format : \n O <input file>");
            System.exit(1);
        } 
        else {*/
            try{
                //InputFile = new FileInputStream(Location.Path);
                InputFile = new FileInputStream("Input.txt");
                Ok = true;
                SourceCode = new byte[InputFile.available()];
                InputFile.read(SourceCode);
                InputFile.close();
                Message = "File opened.";
                Location.Pos = 0;
                Location.Line = 1;
                NextChar();
            }
            catch (IOException e){
                Ok = false;
                Message = "Input file not found.";
            };
        //}
    }
}
