package JavaO;

/**
 *
 * @author nikita
 */

import java.io.InputStream;
//import java.io.IOException;
//import java.io.FileInputStream;
        
        
public class Text {
    
    static final int TabSize = 3;
    static final int CharSpace = ' ';// 32
    static final int CharTab = '\t';//  9
    static final int CharEOL = '\n';//  10
    static final int CharEOT = '\0';//  0
    
    static boolean Ok = false;
    static String Message = "File not opened!";
    static int CurrentChar = CharEOT;
    static int CurrentCharIndex = -1;
    static byte[] SourceCode;
    
//    private static InputStream InputFile;
    
    
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
                    while(++Location.Pos % TabSize != 0){}
                }
            }
        //}
        //catch(IOException e){};
    }
    
    
    static void reset(){
        Location.Pos = 0;
        Location.Line = 1;
        CurrentCharIndex = -1;
        NextChar();
    }
}
