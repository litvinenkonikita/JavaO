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
            //Error.Message(Text.Message);
        }
        //Scan.init();
        //Gen.init();
    }

    public static void main(String[] args){
        System.out.println("\nJavaO compiler...");
        if(args.length == 0){
            Location.Path = null;
        }
        else {
            Location.Path = args[0];
        }
        
        init();
        //Pars.compile();
        //Ovm.run();
    }
    
}
