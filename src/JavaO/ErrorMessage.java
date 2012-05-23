package JavaO;

/**
 *
 * @author nikita
 */

//import java.io.*;

public class ErrorMessage {
    
    private static String Message = new String();
    
    private static boolean Ok = true;
    
    public static void Error(String Msg) throws Exception{
        Message = "Error at " + Location.LexPos + " position " + Location.Line + " line :\n\n" + Msg;
        Ok = false;
        throw new Exception("Compilation error."); // Event!
    }
    
    public static void Expected(String Msg) throws Exception{
        Error(Msg + " expected.");
    }
    
    public static void Warning(String Msg){
        Message = "\nWarning : " + Msg;
    }
    
    public static String getMessage(){
        return Message;
    }
    
    public static boolean getOk(){
        return Ok;
    }
    
    public static void clearMessage(){
        Message = "";
    }
}
