package JavaO;

import JavaO.Tables.Table;

/**
 *
 * @author Litvinenko Nikita
 */

//import java.io.*;
public class JavaO {
    
    static void init() throws Exception {
        Text.reset();
        //if(!Text.Ok){
            //ErrorMessage.Error(Text.Message);
        //}
        Table.init();
        Lexer.init();
        CodeGen.init();
        VM.init();
    }
    
    public static void compile() throws Exception{
        init();
        Syntax.compile();
    }

    public static void run()/* throws Exception */{
        //init();
        //Syntax.compile();
        //if(ErrorMessage.getOk()){
        VM.run();
        //}
    }
    
}
