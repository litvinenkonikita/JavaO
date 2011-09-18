package JavaO;

/**
 *
 * @author nikita
 */

//import java.io.*;

public class ErrorMessage {
    public static void Error(String Msg){
        System.out.println("Error at " + Location.Pos + " position " + Location.Line + " line :\n\n" + Msg);
        System.exit(0);
    }
    
    public static void Expected(String Msg){
        Error(Msg + " expected.");
    }
    
    public static void Warning(String Msg){
        System.out.println("\nWarning : " + Msg);
    }
}
