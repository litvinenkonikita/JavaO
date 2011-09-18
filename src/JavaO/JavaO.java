package JavaO;

/**
 *
 * @author Litvinenko Nikita
 */

//import java.io.*;
public class JavaO {
    
    static void init(){
        Text.reset();
        if(!Text.Ok){
            ErrorMessage.Error(Text.Message);
        }
        Lexer.init();
    }

    public static void run(){
//        System.out.println("\nJavaO compiler...");
//        if(args.length == 0){
//            Location.Path = null;
//        }
//        else {
//            Location.Path = args[0];
//        }
        
        init();
        Syntax.compile();
        VM.run();
    }
    
}
